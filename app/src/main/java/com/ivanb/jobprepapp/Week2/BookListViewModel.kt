package com.ivanb.jobprepapp.Week2

import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivanb.jobprepapp.BookRepository
import com.ivanb.jobprepapp.Week1.Book
import com.ivanb.jobprepapp.Week1.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class BookListViewModel @Inject constructor(
    private val repository: BookRepository
) : ViewModel() {

//    private val _books = MutableStateFlow<List<Book>>(emptyList())
//
//    // Ovo je dostupno Compose ekranu za čitanje
//    val books: StateFlow<List<Book>> = _books.asStateFlow()
//
//    val bookCount: Int get() = _books.value.size
//
//    init {
//        viewModelScope.launch {
//            delay(500) // simulacija dohvaćanja
//            _books.value = sampleBooks
//        }
//    }
//    // U BookListViewModel, dodaj funkciju fun clearBooks() koja postavi _books.value = emptyList().
//    // pozovi je negdje (npr. na klik nekog test-gumba) i provjeri da se UI stvarno isprazni —
//    // dokaz da promjena _books.value unutar ViewModela automatski protječe kroz collectAsState() do UI-a, bez da ručno diraš composable.
//    fun clearBooks(){
//        _books.value = emptyList()
//    }


    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // pamti jel barem jedan refresh ikad uspio
    // "pretražio sam i stvarno nema ništa"

    // Početno stanje
    private val _searchQuery = MutableStateFlow("science fiction")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    private var hasLoadedOnce = false

    init {
        // loadBooks()
        observeBooks()
        refreshBooks(_searchQuery.value)
    }
    private fun observeBooks() {
        viewModelScope.launch {
            repository.books.collect { books ->
                // Ako imamo knjige u bazi, odmah ih prikaži
                if (books.isNotEmpty() || hasLoadedOnce) {
                    _uiState.value = UiState.Success(books.sortedBy { it.title })
                }
                // Ako je baza prazna, ostavi UiState.Loading (ili obradi prazno stanje u UI-u)
            }
        }
    }

    // Promjena teksta u search traci
    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun refreshBooks(query: String = _searchQuery.value) {
        if (query.isBlank()) return
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repository.refreshBooks(query)
                hasLoadedOnce = true
                if (_uiState.value is UiState.Loading) {
                    _uiState.value = UiState.Success(emptyList())
                }
            } catch (e: Exception) {
                // Prikaži grešku SAMO ako Room nema ništa za ponuditi -
                // inače bi pregazili dobar cache samo zato što je refresh pukao
                if (_uiState.value !is UiState.Success) { // provjerava trenutno stanje prije nego prepiše u Error
                    _uiState.value = UiState.Error(mapErrorMessage(e))
                }
            }
        }
    }
    private fun mapErrorMessage(e: Exception): String {
        return when (e) {
            is UnknownHostException -> "Nema internetske veze. Provjeri vezu i pokušaj ponovno."
            is SocketTimeoutException -> "Server ne odgovara. Pokušaj ponovno za koji trenutak."
            is HttpException -> "Greška servera (${e.code()}). Pokušaj kasnije."
            else -> "Nešto je pošlo po zlu: ${e.message}"
        }
    }
    // direktno sa interneta
//    fun loadBooks() {
//        viewModelScope.launch {
//            _uiState.value = UiState.Loading
//            try {
//                val books = repository.searchBooks("science fiction")
//                _uiState.value = UiState.Success(books)
//            } catch (e: Exception) {
//                _uiState.value = UiState.Error("Greška: ${e.message}")
//            }
//        }
//    }

    fun clearBooks() {
        _uiState.value = UiState.Success(emptyList())
    }
}
