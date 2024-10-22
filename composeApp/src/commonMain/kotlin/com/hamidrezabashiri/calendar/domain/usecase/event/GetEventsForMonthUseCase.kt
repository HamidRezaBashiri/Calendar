package com.hamidrezabashiri.calendar.domain.usecase.event

import com.hamidrezabashiri.calendar.domain.model.CalendarEvent
import com.hamidrezabashiri.calendar.domain.repository.CalendarEventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

class GetEventsForMonthUseCase(
    private val repository: CalendarEventRepository
) {
    operator fun invoke(yearMonth: LocalDate): Flow<List<CalendarEvent>> {
        val firstDayOfMonth = LocalDate(yearMonth.year, yearMonth.monthNumber, 1)
        val lastDay = when (yearMonth.monthNumber) {
            2 -> if (isLeapYear(yearMonth.year)) 29 else 28
            4, 6, 9, 11 -> 30
            else -> 31
        }
        val lastDayOfMonth = LocalDate(yearMonth.year, yearMonth.monthNumber, lastDay)

        return repository.getEventsForDateRange(
            firstDayOfMonth.toString(),
            lastDayOfMonth.toString()
        )
    }

    private fun isLeapYear(year: Int): Boolean {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
    }
}