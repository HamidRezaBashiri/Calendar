package com.hamidrezabashiri.calendar.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.atTime
import kotlinx.datetime.toLocalDateTime

data class CalendarEventModel(
    val id: String,
    val title: String,
    val description: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val location: String,
    val isRecurring: Boolean,
    val isReminderSet: Boolean,
    val reminderTimeAndDate: LocalDateTime?, // Nullable reminder time
    val category: String
) {
    // Check if the event is currently ongoing
    fun isOngoing(): Boolean {
        val now = Clock.System.now().toLocalDateTime(timeZone = kotlinx.datetime.TimeZone.currentSystemDefault())
        val startDateTime = startDate.atTime(startTime)
        val endDateTime = endDate.atTime(endTime)
        return now in startDateTime..endDateTime
    }
}
