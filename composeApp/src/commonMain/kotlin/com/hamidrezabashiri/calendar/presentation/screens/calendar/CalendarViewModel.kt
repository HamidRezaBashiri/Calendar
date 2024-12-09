package com.hamidrezabashiri.calendar.presentation.screens.calendar

import androidx.compose.runtime.Composable
import com.hamidrezabashiri.calendar.domain.model.CalendarEventModel
import com.hamidrezabashiri.calendar.domain.usecase.CalendarUseCases
import com.hamidrezabashiri.calendar.presentation.screens.base.BaseViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class CalendarViewModel(
    private val calendarUseCases: CalendarUseCases
) : BaseViewModel<CalendarContract.State, CalendarContract.Intent, CalendarContract.Effect>() {

    // Backing fields for state
    private var events = mutableListOf<CalendarEventModel>()
    private var isLoading = false
    private var error: String? = null
    private var selectedDate: LocalDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date

    init {
        println("CalendarViewModel initialized")
        loadInitialEvents()
    }

    override fun getInitialState() = CalendarContract.State(
        events = emptyList(),
        isLoading = true,
        error = null,
//        selectedDate = selectedDate
    )

    @Composable
    override fun produceUiState(): CalendarContract.State {
        return CalendarContract.State(
            events = events,
            isLoading = isLoading,
            error = error,
//            selectedDate = selectedDate
        )
    }

    private fun loadInitialEvents() {
        viewModelScope.launch {
            try {
                loadEvents(selectedDate)
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    override fun handleIntent(intent: CalendarContract.Intent) {
        viewModelScope.launch {
            when (intent) {
                is CalendarContract.Intent.LoadEvents -> {
                    selectedDate = intent.date
                    loadEvents(intent.date)
                }
                is CalendarContract.Intent.AddEvent -> addEvent(intent.event)
                is CalendarContract.Intent.RefreshEvents -> loadEvents(selectedDate)
//            is CalendarContract.Intent.SelectDate -> {
//                selectedDate = intent.date
//                loadEvents(intent.date)
//            }
                is CalendarContract.Intent.UpdateEventForEditing -> updateEvent(intent.event)
            }
        }
    }

    private fun updateEvent(event: CalendarEventModel) {
        viewModelScope.launch {
            try {
            // Update the state by creating a new instance with the updated event details
            updateState { currentState ->
                currentState.copy(
                    event = event // Set the updated event in the state
                )
            }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun loadEvents(date: LocalDate) {
        viewModelScope.launch {
           isLoading = true

            try {
                calendarUseCases.getEventsForMonth(date).collect { loadedEvents ->
                    updateState { currentState ->
                        currentState.copy(
                            events = loadedEvents // Set all events in the state
                        )
                    }
                 }
                error = null
            } catch (e: Exception) {
                handleError(e)
            } finally {
                isLoading = false
            }
        }
    }

    private fun addEvent(event: CalendarEventModel) {
        viewModelScope.launch {
            try {
                calendarUseCases.addEvent(event)
                emitEffect(CalendarContract.Effect.ShowEventAddedSuccess)
                loadEvents(event.startDate)
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun handleError(e: Exception) {
        error = e.message
        emitEffect(CalendarContract.Effect.ShowError(e.message ?: "An error occurred"))
    }
}