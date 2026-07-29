package com.ivanb.jobprepapp.Week2

import androidx.lifecycle.ViewModel
import com.ivanb.jobprepapp.Week1.Book
import com.ivanb.jobprepapp.Week1.sampleBooks

class BookListViewModel : ViewModel() {
    val books: List<Book> = sampleBooks
}