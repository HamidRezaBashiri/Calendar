package com.hamidrezabashiri.calendar.data.source.local

import com.hamidrezabashiri.calendar.data.entity.CalendarEventEntity
import kotlinx.coroutines.flow.Flow

interface CalendarEventLocalDataSource {
    // Insert a calendar event
    suspend fun insertEvent(event: CalendarEventEntity)

    // Update a calendar event
    suspend fun updateEvent(event: CalendarEventEntity)

    // Delete a calendar event by ID
    suspend fun deleteEvent(eventId: String)

    // Get a calendar event by ID
    suspend fun getEventById(eventId: String): CalendarEventEntity?

    // Get all calendar events (as a Flow for continuous updates)
    fun getAllEvents(): Flow<List<CalendarEventEntity>>

    // Get events by date
    fun getEventsByDate(date: String): Flow<List<CalendarEventEntity>>

    fun getEventsForDateRange(startDate: String, endDate: String): Flow<List<CalendarEventEntity>>

}
