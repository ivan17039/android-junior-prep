package com.ivanb.jobprepapp

import javax.inject.Inject

class BookRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun searchBooks(query: String): List<OpenLibraryBookDto> {
        return apiService.searchBooks(query).docs
    }
}