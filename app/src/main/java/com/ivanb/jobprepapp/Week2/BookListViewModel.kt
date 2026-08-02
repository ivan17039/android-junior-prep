package com.ivanb.jobprepapp.Week2

import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivanb.jobprepapp.Week1.Book
import com.ivanb.jobprepapp.Week1.UiState
import com.ivanb.jobprepapp.Week1.sampleBooks
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookListViewModel @Inject constructor() : ViewModel() {

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

    private val simulateError = false // promijeni na true da testiraš Error stanje

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadBooks()
    }

    fun loadBooks() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            delay(1000)
            if (simulateError) {
                _uiState.value = UiState.Error("Nema internetske veze")
            } else {
                _uiState.value = UiState.Success(sampleBooks)
            }
        }
    }

    fun clearBooks() {
        _uiState.value = UiState.Success(emptyList())
    }
}

@Preview(showBackground = true)
@Composable
fun BookListViewModelPreview() {
    BookListViewModel()
}