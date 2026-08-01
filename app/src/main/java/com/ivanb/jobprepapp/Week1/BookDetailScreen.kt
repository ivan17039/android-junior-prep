package com.ivanb.jobprepapp.Week1

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ivanb.jobprepapp.BookDetailUiState
import com.ivanb.jobprepapp.BookDetailViewModel
import com.ivanb.jobprepapp.Week2.BookListViewModel

@Composable
fun BookDetailScreen(
    viewModel: BookDetailViewModel = viewModel(),
    onBack: () -> Unit,
//    fromScreen: String,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    when (val currentState = state) {
        is BookDetailUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is BookDetailUiState.Success -> {
            Column(modifier = modifier.padding(16.dp)) {
                Button(onClick = onBack) {
                    Text("Natrag")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = currentState.book.title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = "Autor: ${currentState.book.author}")
                Text(text = "Godina izdanja: ${currentState.book.year}")
//                Text(text = "Došli ste s ekrana: $fromScreen") cita se izravno iz Ui Stanja stoga mjenjamo u:
                Text(text = "Došli ste s ekrana: ${currentState.fromScreen}")
            }
        }
        is BookDetailUiState.Error -> {
            Column(modifier = modifier.padding(16.dp)) {
                Button(onClick = onBack) {
                    Text("Natrag")
                }
                Text("Greška: ${currentState.message}")
            }
        }
    }
}