package com.ivanb.jobprepapp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivanb.jobprepapp.Week1.Book
import com.ivanb.jobprepapp.Week1.sampleBooks
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class BookDetailUiState {
    object Loading : BookDetailUiState()
    data class Success(val book: Book, val fromScreen: String) : BookDetailUiState()
    data class Error(val message: String) : BookDetailUiState()
}
@HiltViewModel
class BookDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val bookIndex: Int = savedStateHandle.get<String>("bookIndex")?.toIntOrNull() ?: 0
    private val fromScreen: String = savedStateHandle.get<String>("fromScreen") ?: "unknown"
    private val _uiState = MutableStateFlow<BookDetailUiState>(BookDetailUiState.Loading)
    val uiState: StateFlow<BookDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            delay(500) // simulacija dohvaćanja
            val book = sampleBooks.getOrNull(bookIndex)
            _uiState.value = if (book != null) {
                BookDetailUiState.Success(book = book, fromScreen = fromScreen)
            } else {
                BookDetailUiState.Error("Knjiga nije pronađena")
            }
        }
    }
}