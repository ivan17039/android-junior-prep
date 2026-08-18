package com.ivanb.jobprepapp.Week1

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ivanb.jobprepapp.Week2.BookListViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import com.ivanb.jobprepapp.Spacing

@Composable
fun BookListScreen(
    viewModel: BookListViewModel = hiltViewModel(),
    onBookClick: (Book) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onAboutClick: () -> Unit,
) {
    // 🟢 1. Gumb za povratak na glavni ekran
    BackHandler {
        onBack()
    }

    val state by viewModel.uiState.collectAsState()

    when (val currentState = state) {
        is UiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is UiState.Success -> {
            if (currentState.data.isEmpty()) {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(Spacing.small))
                        Text("Nema pronađenih knjiga")
                    }
                }
            } else {
            LazyColumn(modifier = modifier.fillMaxSize()) {

                // 🟢 2. Zaglavlje s opisom
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(text = "Glavna Aplikacija: Knjižnica")
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "• Klikni na bilo koju knjigu za detalje.\n" +
                                    "• Klikni na 'O aplikaciji' za informacije o autoru."
                        )
                    }
                }

                // 🟢 3. Prikaz ukupnog broja i Test Gumb za resetiranje
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(text = "Ukupan broj knjiga je: ${currentState.data.size}")

                        Button(
                            onClick = { viewModel.clearBooks() },
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Text("Reset / Očisti knjige")
                        }
                    }
                }

                // 🟢 4. Lista knjiga iz currentState.data
                items(currentState.data) { book ->
                    BookCard(book = book, onClick = { onBookClick(book) })
                }

                // 🟢 5. Gumbi na dnu (O aplikaciji & Natrag)
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(onClick = onAboutClick, modifier = Modifier.weight(1f)) {
                            Text("O aplikaciji")
                        }
                        Spacer(modifier = Modifier.padding(Spacing.small))
                        Button(onClick = onBack, modifier = Modifier.weight(1f)) {
                            Text("Natrag")
                        }
                    }
                }
            }
        }
        }

        is UiState.Error -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Greška: ${currentState.message}")
                Spacer(modifier = Modifier.height(Spacing.small))
                Button(onClick = { viewModel.refreshBooks() }) {
                    Text("Pokušaj ponovno")
                }
                Spacer(modifier = Modifier.height(Spacing.small))
                Button(onClick = onBack) {
                    Text("Natrag")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookListScreenPreview() {
    BookListScreen(
        onBookClick = {},
        onBack = {},
        onAboutClick = {}
    )
}