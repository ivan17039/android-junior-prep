package com.ivanb.jobprepapp.Week1

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BookScreen(books: List<Book>, modifier: Modifier = Modifier) {
    var count by remember {mutableStateOf(0)  }
    LazyColumn {

        item {
            Row{
                Button(onClick = {count++ }){
                    Text("Kliknuto $count puta")

                }
                Button(onClick = {count=0}){
                    Text("Reset")
                }
            }
        }

        items(books) { book ->
            BookCard(book = book)
        }

        item {
            Text(
                text = "Lista brojeva:",
                modifier = Modifier.padding(16.dp)
            )
        }

        items((1..50).toList()) { number ->
            Text(
                text = "Broj: $number",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 1200)
@Composable
fun BookScreenPreview() {
    BookScreen(books = sampleBooks)
}