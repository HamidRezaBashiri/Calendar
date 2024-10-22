package com.hamidrezabashiri.calendar.data.repository

import com.hamidrezabashiri.calendar.data.mapper.toDomainModel
import com.hamidrezabashiri.calendar.data.mapper.toEntity
import com.hamidrezabashiri.calendar.data.source.local.CalendarEventLocalDataSource
import com.hamidrezabashiri.calendar.data.source.remote.CalendarEventRemoteDataSource
import com.hamidrezabashiri.calendar.domain.model.CalendarEvent
import com.hamidrezabashiri.calendar.domain.repository.CalendarEventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CalendarEventRepositoryImpl(
    private val localDataSource: CalendarEventLocalDataSource,
    private val remoteDataSource: CalendarEventRemoteDataSource
) : CalendarEventRepository {

    override suspend fun insertEvent(event: CalendarEvent) {
        localDataSource.insertEvent(event.toEntity())
        remoteDataSource.syncUserEvent(event.toEntity())
    }

    override suspend fun updateEvent(event: CalendarEvent) {
        localDataSource.updateEvent(event.toEntity())
        remoteDataSource.syncUserEvent(event.toEntity())
    }

    override suspend fun deleteEvent(eventId: String) {
        localDataSource.deleteEvent(eventId)
    }

    override suspend fun getEventById(eventId: String): CalendarEvent? =
        localDataSource.getEventById(eventId)?.toDomainModel()

    override fun getAllEvents(): Flow<List<CalendarEvent>> =
        localDataSource.getAllEvents().map { events ->
            events.map { it.toDomainModel() }
        }

    override fun getEventsByDate(date: String): Flow<List<CalendarEvent>> =
        localDataSource.getEventsByDate(date).map { events ->
            events.map { it.toDomainModel() }
        }

    override fun getEventsForDateRange(
        startDate: String,
        endDate: String
    ): Flow<List<CalendarEvent>> =
        localDataSource.getEventsForDateRange(startDate, endDate).map { events ->
            events.map { it.toDomainModel() }
        }

    override suspend fun syncWithRemote(countryCode: String) {
        val remoteEvents = remoteDataSource.fetchCountrySpecificEvents(countryCode)
        remoteEvents.forEach { localDataSource.insertEvent(it) }
    }
}

