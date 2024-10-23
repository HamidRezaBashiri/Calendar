package com.hamidrezabashiri.calendar.domain.usecase.event

import com.hamidrezabashiri.calendar.domain.model.CalendarEventModel
import com.hamidrezabashiri.calendar.domain.repository.CalendarEventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

class GetEventsForDateUseCase(
    private val repository: CalendarEventRepository
) {
    operator fun invoke(date: LocalDate): Flow<List<CalendarEventModel>> =
        repository.getEventsByDate(date.toString())
}