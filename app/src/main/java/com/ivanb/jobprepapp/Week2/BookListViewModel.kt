package com.ivanb.jobprepapp.Week2

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivanb.jobprepapp.Week1.Book
import com.ivanb.jobprepapp.Week1.sampleBooks
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookListViewModel : ViewModel() {

    private val _books = MutableStateFlow<List<Book>>(emptyList())

    // Ovo je dostupno Compose ekranu za čitanje
    val books: StateFlow<List<Book>> = _books.asStateFlow()

    val bookCount: Int get() = _books.value.size

    init {
        viewModelScope.launch {
            delay(500) // simulacija dohvaćanja
            _books.value = sampleBooks
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookListViewModelPreview() {
    BookListViewModel()
}