package com.hamidrezabashiri.calendar.presentation.screens.library

import com.hamidrezabashiri.calendar.domain.model.BookModel
import com.hamidrezabashiri.calendar.presentation.screens.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LibraryViewModel(
//    private val calendarUseCases: CalendarUseCases
) : BaseViewModel<LibraryContract.State, LibraryContract.Intent, LibraryContract.Effect>() {

    private val _state = MutableStateFlow(LibraryContract.State())

    override val state = produceState(LibraryContract.State()) {
        _state.value
    }

    override val effects = MutableSharedFlow<LibraryContract.Effect>()

    init {
        println("LibraryViewModel initialized") // Example log statement
        loadInitialBooks()
    }

    private fun loadInitialBooks() {
        viewModelScope.launch {
            try {
//                val currentDate = kotlinx.datetime.Clock.System.now()
//                    .toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
//                    .date
                loadBooks()
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    override fun handleIntent(intent: LibraryContract.Intent) {
        when (intent) {
            is LibraryContract.Intent.LoadBooks -> loadBooks()
            LibraryContract.Intent.RefreshBooks -> TODO()
        }
    }

    private fun loadBooks() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
//                val events = calendarUseCases.getEventsForDate(date)
//                    .first() // Convert Flow to List by taking first emission
                val books = listOf(
                    BookModel(
                        id = 1,
                        title = "Book 1",
                        author = "Author 1",
                        description = "Description 1",
                    ), BookModel(
                        id = 2,
                        title = "Book 2",
                        author = "Author 2",
                        description = "Description 2",
                    ), BookModel(
                        id = 3,
                        title = "Book 3",
                        author = "Author 3",
                        description = "Description 3",
                    ), BookModel(
                        id = 4,
                        title = "Book 4",
                        author = "Author 4",
                        description = "Description 4",
                    )
                )

                _state.update {
                    it.copy(
                        books = books, isLoading = false, error = null
                    )
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun openBookReader(bookId : Int) {
        viewModelScope.launch {
            try {
//                calendarUseCases.addEvent(event)
//                effects.emit(CalendarContract.Effect.ShowEventAddedSuccess)
//                loadEvents(event.startDate) // Reload events for the date
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private suspend fun handleError(e: Exception) {
        effects.emit(LibraryContract.Effect.ShowError(e.message ?: "An error occurred"))
        _state.update {
            it.copy(
                isLoading = false, error = e.message
            )
        }
    }
}