package com.ivanb.jobprepapp

import android.R.attr.onClick
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun BookCard(
    book: Book,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
){    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable{onClick()}
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = book.title, fontWeight = FontWeight.Bold)
            Text(text = book.author)
            Text(text = book.year.toString())
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookCardPreview() {
    BookCard(book = Book("Dune", "Frank Herbert", 1965))
}