package com.ivanb.jobprepapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BookDetailScreen(
    book: Book,
    onBack: () -> Unit,
    fromScreen: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        Button(onClick = onBack) {
            Text("Natrag")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = book.title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = "Autor: ${book.author}")
        Text(text = "Godina izdanja: ${book.year}")

        Text(
            text = "Došli ste s ekrana: $fromScreen"
        )
    }
}