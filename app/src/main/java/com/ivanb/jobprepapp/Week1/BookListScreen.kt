package com.ivanb.jobprepapp.Week1

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onAboutClick: () -> Unit
) {
    BackHandler {
        onBack() // Omogućuje da se vrati na MojEkran sa tipkom povratka na mobitelu
    }
    LazyColumn(modifier = modifier) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "• Klikni na bilo koju knjigu za detalje.\n" +
                            "• Klikni na 'O aplikaciji' za informacije o autoru.\n" +
                            "• Klikni na 'Vrati se na vježbe' za izlazak na igralište.",

                )
                Text(
                    text = " Glavna Aplikacija: Knjižnica",

                )

            }
        }
        items(books) { book ->
            BookCard(book = book, onClick = { onBookClick(book) })
        }
        item {
            Row(modifier = Modifier.padding(16.dp)) {
                Button(onClick = onAboutClick, modifier = Modifier.padding(16.dp)) {
                    Text("O aplikaciji")
                }
                Button(onClick = onBack, modifier = Modifier.padding(16.dp)) {
                    Text("Natrag")
                }
            }
        }

    }
}