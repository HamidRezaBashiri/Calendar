package com.hamidrezabashiri.calendar.presentation.screens.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.hamidrezabashiri.calendar.domain.model.BookModel
import com.hamidrezabashiri.calendar.presentation.screens.base.BaseViewModel
import kotlinx.coroutines.launch

class LibraryViewModel : BaseViewModel<LibraryContract.State, LibraryContract.Intent, LibraryContract.Effect>() {

    // Use Compose state to trigger recomposition
    private var books by mutableStateOf<List<BookModel>>(emptyList())
    private var isLoading by mutableStateOf(false)
    private var error by mutableStateOf<String?>(null)

    init {
        println("LibraryViewModel initialized")
        loadInitialBooks()
    }

    override fun getInitialState() = LibraryContract.State(
        books = emptyList(),
        isLoading = true,
        error = null
    )

    @Composable
    override fun produceUiState(): LibraryContract.State {
        return LibraryContract.State(
            books = books,
            isLoading = isLoading,
            error = error
        )
    }

    private fun loadInitialBooks() {
        viewModelScope.launch {
            try {
                loadBooks()
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    override fun handleIntent(intent: LibraryContract.Intent) {
        when (intent) {
            is LibraryContract.Intent.LoadBooks -> loadBooks()
            LibraryContract.Intent.RefreshBooks -> loadBooks()
        }
    }

    private fun loadBooks() {
        viewModelScope.launch {
            isLoading = true

            try {
                // Simulate network delay
//                kotlinx.coroutines.delay(1000)

                val loadedBooks = listOf(
                    BookModel(
                        id = 1,
                        title = "Book 1",
                        author = "Author 1",
                        description = "Description 1",
                    ),
                    BookModel(
                        id = 2,
                        title = "Book 2",
                        author = "Author 2",
                        description = "Description 2",
                    ),
                    BookModel(
                        id = 3,
                        title = "Book 3",
                        author = "Author 3",
                        description = "Description 3",
                    ),
                    BookModel(
                        id = 4,
                        title = "Book 4",
                        author = "Author 4",
                        description = "Description 4",
                    )
                )

                books = loadedBooks // Use assignment to trigger state update
                error = null
            } catch (e: Exception) {
                handleError(e)
            } finally {
                isLoading = false
            }
        }
    }

    private fun handleError(e: Exception) {
        error = e.message
        emitEffect(LibraryContract.Effect.ShowError(e.message ?: "An error occurred"))
    }
}