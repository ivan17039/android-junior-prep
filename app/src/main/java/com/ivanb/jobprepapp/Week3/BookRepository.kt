package com.ivanb.jobprepapp

import com.ivanb.jobprepapp.Week1.Book
import com.ivanb.jobprepapp.Week3.BookDao
import com.ivanb.jobprepapp.Week3.BookEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val apiService: ApiService,
    private val bookDao: BookDao
) {
    val books: Flow<List<Book>> = bookDao.getAll().map { entities ->
        entities.map { it.toBook() }
    }
    suspend fun refreshBooks(query: String) {
        val response = apiService.searchBooks(query)
        val books = response.docs.map { it.toBook() }
        bookDao.deleteAll()
        bookDao.insertAll(books.map { it.toEntity() })
    }
    suspend fun searchBooks(query: String): List<Book> {
        val response = apiService.searchBooks(query)
        return response.docs.map { it.toBook() } // Pretvara List<OpenLibraryBookDto> u List<Book>, element po element.
    }
}
private fun OpenLibraryBookDto.toBook(): Book {
    return Book(
        title = title,
        author = authorName?.firstOrNull() ?: "Nepoznat autor",
        year = firstPublishYear ?: 0,
        coverUrl = coverId?.let { "https://covers.openlibrary.org/b/id/$it-M.jpg" } // ako coverId postoji, izgradi URL; ako je null, coverUrl ostaje nul
    )
}

private fun BookEntity.toBook(): Book {
    return Book(title = title, author = author, year = year, coverUrl = coverUrl)
}

private fun Book.toEntity(): BookEntity {
    return BookEntity(title = title, author = author, year = year, coverUrl = coverUrl)
}