package com.ivanb.jobprepapp.Week2

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivanb.jobprepapp.Week1.Book
import com.ivanb.jobprepapp.Week1.sampleBooks
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BookListViewModel : ViewModel() {
    val books: List<Book> = sampleBooks
    val bookCount: Int get() = books.size

    init {
        viewModelScope.launch {
            delay(500)
            Log.d("MOJ_TAG", "ViewModel podaci spremni")
            println("ViewModel podaci spremni")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookListViewModelPreview() {
    BookListViewModel()
}