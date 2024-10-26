package com.hamidrezabashiri.calendar.presentation.screens.calendar

import com.hamidrezabashiri.calendar.domain.model.CalendarEventModel
import com.hamidrezabashiri.calendar.domain.usecase.CalendarUseCases
import com.hamidrezabashiri.calendar.presentation.screens.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDateTime

class CalendarViewModel(
    private val calendarUseCases: CalendarUseCases
) : BaseViewModel<CalendarContract.State, CalendarContract.Intent, CalendarContract.Effect>() {

    private val _state = MutableStateFlow(CalendarContract.State())

    override val state = produceState(CalendarContract.State()) {
        _state.value
    }

    override val effects = MutableSharedFlow<CalendarContract.Effect>()

    init {
        println("CalendarViewModel initialized") // Example log statement
        loadInitialEvents()

    }

    private fun loadInitialEvents() {
        viewModelScope.launch {
            try {
                val currentDate = kotlinx.datetime.Clock.System.now()
                    .toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
                    .date
                loadEvents(currentDate)
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    override fun handleIntent(intent: CalendarContract.Intent) {
        when (intent) {
            is CalendarContract.Intent.LoadEvents -> loadEvents(intent.date)
            is CalendarContract.Intent.AddEvent -> addEvent(intent.event)
            is CalendarContract.Intent.RefreshEvents -> loadInitialEvents()
        }
    }

    private fun loadEvents(date: LocalDate) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val events = calendarUseCases.getEventsForDate(date)
                    .first() // Convert Flow to List by taking first emission

                _state.update { it.copy(
                    events = events,
                    isLoading = false,
                    error = null
                ) }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun addEvent(event: CalendarEventModel) {
        viewModelScope.launch {
            try {
                calendarUseCases.addEvent(event)
                effects.emit(CalendarContract.Effect.ShowEventAddedSuccess)
                loadEvents(event.startDate) // Reload events for the date
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private suspend fun handleError(e: Exception) {
        effects.emit(CalendarContract.Effect.ShowError(e.message ?: "An error occurred"))
        _state.update { it.copy(
            isLoading = false,
            error = e.message
        ) }
    }
}