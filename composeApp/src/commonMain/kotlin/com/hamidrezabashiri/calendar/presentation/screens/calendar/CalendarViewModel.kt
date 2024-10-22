package com.hamidrezabashiri.calendar.presentation.screens.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamidrezabashiri.calendar.domain.model.CalendarEvent
import com.hamidrezabashiri.calendar.domain.usecase.CalendarUseCases
import com.hamidrezabashiri.calendar.domain.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

data class CalendarState(
    val selectedDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    val events: List<CalendarEvent> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class CalendarViewModel(
    private val calendarUseCases: CalendarUseCases
) : ViewModel() {
    private val _state = MutableStateFlow(CalendarState())
    val state = _state.asStateFlow()

    init {
        loadEventsForCurrentMonth()
    }

    fun onDateSelected(date: LocalDate) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                selectedDate = date,
                isLoading = true
            )

            calendarUseCases.getEventsForDate(date)
                .collect { events ->
                    _state.value = _state.value.copy(
                        events = events,
                        isLoading = false
                    )
                }
        }
    }

    fun addEvent(event: CalendarEvent) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            when (val result = calendarUseCases.addEvent(event)) {
                is Result.Success -> {
                    loadEventsForCurrentMonth()
                }
                is Result.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.exception.message
                    )
                }
            }
        }
    }

    private fun loadEventsForCurrentMonth() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            calendarUseCases.getEventsForMonth(_state.value.selectedDate)
                .collect { events ->
                    _state.value = _state.value.copy(
                        events = events,
                        isLoading = false
                    )
                }
        }
    }

    fun deleteEvent(eventId: String) {
        viewModelScope.launch {
            when (val result = calendarUseCases.deleteEvent(eventId)) {
                is Result.Success -> loadEventsForCurrentMonth()
                is Result.Error -> {
                    _state.value = _state.value.copy(
                        error = result.exception.message
                    )
                }
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}