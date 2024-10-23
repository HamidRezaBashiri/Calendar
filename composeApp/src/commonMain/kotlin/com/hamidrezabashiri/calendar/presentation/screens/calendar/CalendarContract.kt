package com.hamidrezabashiri.calendar.presentation.screens.calendar

import com.hamidrezabashiri.calendar.domain.model.CalendarEventModel
import com.hamidrezabashiri.calendar.util.UiEffect
import com.hamidrezabashiri.calendar.util.UiIntent
import com.hamidrezabashiri.calendar.util.UiState
import kotlinx.datetime.LocalDate

object CalendarContract {
    data class State(
        val events: List<CalendarEventModel> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    ) : UiState

    sealed interface Intent : UiIntent {
        data class LoadEvents(val date: LocalDate) : Intent
        data class AddEvent(val event: CalendarEventModel) : Intent
        data object RefreshEvents : Intent
    }

    sealed interface Effect : UiEffect {
        data class ShowError(val message: String) : Effect
        data class NavigateToEventDetail(val eventId: String) : Effect
        data object ShowEventAddedSuccess : Effect
    }
}