package com.hamidrezabashiri.calendar.domain.usecase.event

import com.hamidrezabashiri.calendar.domain.model.CalendarEvent
import com.hamidrezabashiri.calendar.domain.repository.CalendarEventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

class GetEventsForDateUseCase(
    private val repository: CalendarEventRepository
) {
    operator fun invoke(date: LocalDate): Flow<List<CalendarEvent>> =
        repository.getEventsByDate(date.toString())
}