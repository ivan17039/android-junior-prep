package com.ivanb.jobprepapp

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BookListScreen(
    books: List<Book>,
    onBookClick: (Book) -> Unit,
    modifier: Modifier = Modifier,
    onAboutClick: () -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(books) { book ->
            BookCard(book = book, onClick = { onBookClick(book) })
        }
        item{
            Button(onClick = onAboutClick, modifier= Modifier.padding(16.dp)){
                Text("O aplikaciji")
            }
        }
    }
}