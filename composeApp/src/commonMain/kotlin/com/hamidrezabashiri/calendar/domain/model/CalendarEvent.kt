package com.hamidrezabashiri.calendar.domain.model

class CalendarEvent(
    val id: String,
    val title: String,
    val startTime: String,
    val endTime: String,
) {

//    fun isOngoing(): Boolean {
//        val now = LocalDateTime.
//        return now.isAfter(startTime) && now.isBefore(endTime)
//    }
//
//    fun getNextOccurrence(after: LocalDateTime): LocalDateTime? {
//         Complex logic to calculate next occurrence based on recurrence pattern
//    }
}