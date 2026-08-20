package com.ivanb.jobprepapp

import com.ivanb.jobprepapp.Week3.BookDao
import com.ivanb.jobprepapp.Week3.BookEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.net.UnknownHostException

class BookRepositoryTest {

    @Test
    fun `refreshBooks mapira DTO u Entity i sprema u Room`() = runTest {
        val dto = OpenLibraryBookDto(
            key = "/works/OL12345W",
            title = "Dune",
            authorName = listOf("Frank Herbert"),
            firstPublishYear = 1965,
            coverId = 12547191
        )
        val expectedEntity = BookEntity(
            id = "/works/OL12345W",
            title = "Dune",
            author = "Frank Herbert",
            year = 1965,
            coverUrl = "https://covers.openlibrary.org/b/id/12547191-M.jpg"
        )
        val mockApiService = mockk<ApiService>()
        val mockDao = mockk<BookDao>(relaxUnitFun = true)
        coEvery { mockApiService.searchBooks(any()) } returns SearchResponse( docs = listOf(dto))
        every { mockDao.getAll() } returns flowOf(emptyList())

        val repository = BookRepository(mockApiService, mockDao)
        repository.refreshBooks("dune")

        coVerify { mockDao.insertAll(listOf(expectedEntity)) }
    }

    @Test
    fun `refreshBooks propagira mrežnu grešku umjesto da je proguta`() = runTest {
        val mockApiService = mockk<ApiService>()
        val mockDao = mockk<BookDao>(relaxUnitFun = true)
        coEvery { mockApiService.searchBooks(any()) } throws UnknownHostException()
        every { mockDao.getAll() } returns flowOf(emptyList())

        val repository = BookRepository(mockApiService, mockDao)
        var thrown = false
        try {
            repository.refreshBooks("dune")
        } catch (e: UnknownHostException) {
            thrown = true
        }
        assertTrue(thrown)
    }
}