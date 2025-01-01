package com.hamidrezabashiri.calendar.presentation.common.components

import com.hamidrezabashiri.calendar.domain.model.CalendarEventModel
import kotlinx.datetime.LocalDate

object EventMapper {
    fun mapEventsToIndicators(events: List<CalendarEventModel>, date: LocalDate): List<EventIndicator> {
        return events
            .filter { it.startDate == date }
            .mapNotNull { event ->
                when (event.category) {
                    "PUBLIC_HOLIDAY" -> EventIndicator.PUBLIC_HOLIDAY
                    "OBSERVANCE" -> EventIndicator.OBSERVANCE
                    "SEASON" -> EventIndicator.SEASON
                    else -> null
                }
            }
            .distinct()
    }
}
