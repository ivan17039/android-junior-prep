package com.ivanb.jobprepapp

import com.ivanb.jobprepapp.Week1.Book
import com.ivanb.jobprepapp.Week1.UiState
import com.ivanb.jobprepapp.Week2.BookListViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

@OptIn(ExperimentalCoroutinesApi::class)
class BookListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    // setup/tearDown - bez ovog koda test bi pukao odmah na pokretanju jel viewModelScope korirsti Dispatcher.Main kojeg tester nema
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `kad repository ima knjige, uiState postaje Success`() = runTest {
        val testBooks = listOf(Book("Dune", "Frank Herbert", 1965, null))
        val mockRepository = mockk<BookRepository>()
        every { mockRepository.books } returns flowOf(testBooks)
        coEvery { mockRepository.refreshBooks(any()) } returns Unit

        val viewModel = BookListViewModel(mockRepository)
        advanceUntilIdle() // pusti sve pokrenute coroutine-ove da završe (brzo, bez pravog čekanja), pa javi kad je gotovo

        assertEquals(UiState.Success(testBooks), viewModel.uiState.value)
    }

    @Test
    fun `kad refreshBooks baci grešku i nema cachea, uiState postaje Error`() = runTest {
        val mockRepository = mockk<BookRepository>()
        every { mockRepository.books } returns flowOf(emptyList())
        coEvery { mockRepository.refreshBooks(any()) } throws UnknownHostException()

        val viewModel = BookListViewModel(mockRepository)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is UiState.Error)
    }
}