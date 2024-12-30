package com.hamidrezabashiri.calendar.presentation.screens.library

import com.hamidrezabashiri.calendar.domain.model.BookModel
import com.hamidrezabashiri.calendar.util.UiEffect
import com.hamidrezabashiri.calendar.util.UiIntent
import com.hamidrezabashiri.calendar.util.UiState

object LibraryContract {
    data class State(
        val books: List<BookModel> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    ) : UiState

    sealed interface Intent : UiIntent {
        data class OpenBook(val bookId: Int) : Intent
        data object LoadBooks : Intent
        data object RefreshBooks : Intent
    }

    sealed interface Effect : UiEffect {
        data class ShowError(val message: String) : Effect
        data class NavigateToBookReader(val bookId: String) : Effect
    }
}