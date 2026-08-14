package com.ivanb.jobprepapp.Week1

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import coil3.compose.AsyncImage
import com.ivanb.jobprepapp.R
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
    Row(modifier = Modifier.padding(16.dp)) {
        AsyncImage(
            model = book.coverUrl,
            contentDescription = book.title,
            placeholder = painterResource(R.drawable.ic_launcher_foreground),
            error = painterResource(R.drawable.ic_launcher_foreground),

        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = book.title, fontWeight = FontWeight.Bold)
            Text(text = book.author)
            Text(text = book.year.toString())
        }
    }
    }
}

@Preview(showBackground = true)
@Composable
fun BookCardPreview() {
    BookCard(book = Book(id="1","Dune", "Frank Herbert", 1965))
}