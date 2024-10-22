package com.hamidrezabashiri.calendar.domain.repository

import com.hamidrezabashiri.calendar.domain.model.CalendarEvent
import kotlinx.coroutines.flow.Flow

interface CalendarEventRepository {
    suspend fun insertEvent(event: CalendarEvent)
    suspend fun updateEvent(event: CalendarEvent)
    suspend fun deleteEvent(eventId: String)
    suspend fun getEventById(eventId: String): CalendarEvent?
    fun getAllEvents(): Flow<List<CalendarEvent>>
    fun getEventsByDate(date: String): Flow<List<CalendarEvent>>
    fun getEventsForDateRange(startDate: String, endDate: String): Flow<List<CalendarEvent>>
    suspend fun syncWithRemote(countryCode: String)
}