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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.ivanb.jobprepapp.Spacing
import com.ivanb.jobprepapp.Week2.BookListViewModel

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
    val query by viewModel.searchQuery.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    // Glavni vertikalni kontejner - SearchBar ide na vrh, a sadržaj ispod
    Column(modifier = modifier.fillMaxSize()) {

        // 🟢 2. TRAKA ZA PRETRAŽIVANJE (SearchBar)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                modifier = Modifier.weight(1f),
                label = { Text("Pretraži knjige") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        keyboardController?.hide()
                        viewModel.refreshBooks()
                    }
                )
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Button(
                onClick = {
                    keyboardController?.hide()
                    viewModel.refreshBooks()
                }
            ) {
                Text("Traži")
            }
        }

        // 🟢 3. PRIKAZ SADRŽAJA OVISNO O UI STANJU
        Box(modifier = Modifier.weight(1f)) {
            when (val currentState = state) {
                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is UiState.Success -> {
                    if (currentState.data.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(Spacing.small))
                                Text("Nema pronađenih knjiga za '$query'")
                            }
                        }
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {

                            // Zaglavlje s opisom
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Text(text = "Glavna Aplikacija: Knjižnica")
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "• Utipkaj pojam i klikni 'Traži'.\n" +
                                                "• Klikni na bilo koju knjigu za detalje."
                                    )
                                }
                            }

                            // Prikaz ukupnog broja i gumb za resetiranje
                            item {
                                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                                    Text(text = "Ukupan broj knjiga je: ${currentState.data.size}")

                                    Button(
                                        onClick = { viewModel.clearBooks() },
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    ) {
                                        Text("Reset / Očisti knjige")
                                    }
                                }
                            }

                            // Lista knjiga
                            items(currentState.data) { book ->
                                BookCard(book = book, onClick = { onBookClick(book) })
                            }

                            // Gumbi na dnu
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
                        modifier = Modifier
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