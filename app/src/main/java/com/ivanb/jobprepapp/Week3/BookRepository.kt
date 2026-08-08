package com.ivanb.jobprepapp

import com.ivanb.jobprepapp.Week1.Book
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun searchBooks(query: String): List<Book> {
        val response = apiService.searchBooks(query)
        return response.docs.map { it.toBook() } // Pretvara List<OpenLibraryBookDto> u List<Book>, element po element.
    }
}
private fun OpenLibraryBookDto.toBook(): Book {
    return Book(
        title = title,
        author = authorName?.firstOrNull() ?: "Nepoznat autor",
        year = firstPublishYear ?: 0
    )
}