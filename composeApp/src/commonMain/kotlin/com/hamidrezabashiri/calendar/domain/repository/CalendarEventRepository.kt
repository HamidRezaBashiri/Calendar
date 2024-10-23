package com.hamidrezabashiri.calendar.domain.repository

import com.hamidrezabashiri.calendar.domain.model.CalendarEventModel
import kotlinx.coroutines.flow.Flow

interface CalendarEventRepository {
    suspend fun insertEvent(event: CalendarEventModel)
    suspend fun updateEvent(event: CalendarEventModel)
    suspend fun deleteEvent(eventId: String)
    suspend fun getEventById(eventId: String): CalendarEventModel?
    fun getAllEvents(): Flow<List<CalendarEventModel>>
    fun getEventsByDate(date: String): Flow<List<CalendarEventModel>>
    fun getEventsForDateRange(startDate: String, endDate: String): Flow<List<CalendarEventModel>>
    suspend fun syncWithRemote(countryCode: String)
}