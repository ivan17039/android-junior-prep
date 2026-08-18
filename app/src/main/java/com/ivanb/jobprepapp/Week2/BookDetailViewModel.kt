package com.ivanb.jobprepapp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivanb.jobprepapp.Week1.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed class BookDetailUiState {
    object Loading : BookDetailUiState()
    data class Success(val book: Book, val fromScreen: String) : BookDetailUiState()
    data class Error(val message: String) : BookDetailUiState()
}
@HiltViewModel
class BookDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: BookRepository
) : ViewModel() {

    // 1. Čitamo "bookId" kao Int (točno ono ime koje smo dali u AppNavigation)
    private val bookId: String =
        savedStateHandle.get<String>("bookId") ?: ""
    private val fromScreen: String = savedStateHandle.get<String>("fromScreen") ?: "unknown"
    private val _uiState = MutableStateFlow<BookDetailUiState>(BookDetailUiState.Loading)
    val uiState: StateFlow<BookDetailUiState> = _uiState.asStateFlow()
    init {
        loadBook()
    }
    private fun loadBook() {
        viewModelScope.launch {
            _uiState.value = BookDetailUiState.Loading

            try {
                // Take only the first emission from the repository flow
                val books = repository.books.first()

                val foundBook = books.find { it.id == bookId }
                if (foundBook != null) {
                    _uiState.value = BookDetailUiState.Success(
                        book = foundBook,
                        fromScreen = fromScreen
                    )
                } else {
                    _uiState.value = BookDetailUiState.Error("Knjiga nije pronađena.")
                }
            } catch (e: Exception) {
                _uiState.value = BookDetailUiState.Error("Greška pri učitavanju: ${e.localizedMessage}")
            }
        }
    }
}