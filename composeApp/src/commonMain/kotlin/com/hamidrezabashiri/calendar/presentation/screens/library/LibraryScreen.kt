package com.hamidrezabashiri.calendar.presentation.screens.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hamidrezabashiri.calendar.di.ViewModelProvider
import com.hamidrezabashiri.calendar.domain.model.BookModel
import com.hamidrezabashiri.calendar.presentation.screens.base.BaseScreen

@Composable
fun LibraryScreen() {
    val viewModel = ViewModelProvider.provideLibraryViewModel()

    BaseScreen(
        viewModel = viewModel,
        effectHandler = { effect ->
            when (effect) {
                is LibraryContract.Effect.NavigateToBookReader -> {
                    // Handle navigation
                    println("Navigating to book reader for book ${effect.bookId}")
                }
                is LibraryContract.Effect.ShowError -> {
                    // Show error (implement your error handling UI)
                    println("Error: ${effect.message}")
                }
            }
        }
    ) { state, onIntent ->
        LibraryContent(
            state = state,
            onBookClick = { bookId ->
//                onIntent(LibraryContract.Intent.OpenBook(bookId))
            },
            onRefresh = {
                onIntent(LibraryContract.Intent.RefreshBooks)
            }
        )
    }
}

@Composable
private fun LibraryContent(
    state: LibraryContract.State,
    onBookClick: (Int) -> Unit,
    onRefresh: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator()
            }
            state.error != null -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Error: ${state.error}")
                    Text(
                        "Tap to retry",
                        modifier = Modifier.clickable { onRefresh() }
                    )
                }
            }
            state.books.isEmpty() -> {
                Text("No books available")
            }
            else -> {
                BookList(
                    books = state.books,
                    onBookClick = onBookClick
                )
            }
        }
    }
}

@Composable
private fun BookList(
    books: List<BookModel>,
    onBookClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        books.forEach { book ->
            BookItem(
                book = book,
                onClick = { onBookClick(book.id) }
            )
        }
    }
}

@Composable
private fun BookItem(
    book: BookModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = book.title)
            Text(text = "by ${book.author}")
            book.description?.let { description ->
                Text(text = description)
            }
        }
    }
}