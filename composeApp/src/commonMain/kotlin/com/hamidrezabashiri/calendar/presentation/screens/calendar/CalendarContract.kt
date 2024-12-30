package com.hamidrezabashiri.calendar.presentation.screens.calendar

import com.hamidrezabashiri.calendar.domain.model.CalendarEventModel
import com.hamidrezabashiri.calendar.util.UiEffect
import com.hamidrezabashiri.calendar.util.UiIntent
import com.hamidrezabashiri.calendar.util.UiState
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDateTime

object CalendarContract {
    data class State(
        val events: List<CalendarEventModel> = emptyList(),
        val event: CalendarEventModel = CalendarEventModel(
            id = "",
            title = "",
            description = "",
            startDate = Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).date,
            endDate = Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).date,
            startTime = Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).time,
            endTime = Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).time,
            location = "",
            isRecurring = false,
            isReminderSet = false,
            reminderTimeAndDate = null,
            category = ""
        ),
        val isLoading: Boolean = false,
        val error: String? = null,
        val selectedDate: LocalDate? = null,
        val currentMonthOffset: Int = 0
    ) : UiState

    sealed interface Intent : UiIntent {
        data class LoadEvents(val date: LocalDate) : Intent
        data class AddEvent(val event: CalendarEventModel) : Intent
        data class UpdateEventForEditing(val event: CalendarEventModel) : Intent
        data object RefreshEvents : Intent
        data class FetchHolidays(val year: Int) : Intent
        data class SelectDate(val date: LocalDate) : Intent
        data class UpdateMonthOffset(val offset: Int) : Intent
    }

    sealed interface Effect : UiEffect {
        data class ShowError(val message: String) : Effect
        data class NavigateToEventDetail(val eventId: String) : Effect
        data object ShowEventAddedSuccess : Effect
    }
}