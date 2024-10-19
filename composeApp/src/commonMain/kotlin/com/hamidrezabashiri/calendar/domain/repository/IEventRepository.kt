package com.hamidrezabashiri.calendar.domain.repository

import com.hamidrezabashiri.calendar.data.entity.CalendarEventEntity
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface IEventRepository {
    fun getAllEvents(): Flow<List<CalendarEventEntity>>
    suspend fun addEvent(calendarEventEntity: CalendarEventEntity)
    suspend fun updateEvent(calendarEventEntity: CalendarEventEntity)
    suspend fun deleteEvent(id: ObjectId)
    fun getEventsByDate(date: Long): Flow<List<CalendarEventEntity>>
    fun getEventsByCountry(countryCode: String): Flow<List<CalendarEventEntity>>
    fun getUniversalEvents(): Flow<List<CalendarEventEntity>>
}