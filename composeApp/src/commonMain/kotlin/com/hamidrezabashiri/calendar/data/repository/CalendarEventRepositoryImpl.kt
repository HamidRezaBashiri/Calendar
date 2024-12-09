package com.hamidrezabashiri.calendar.data.repository

import com.hamidrezabashiri.calendar.data.mapper.toDatabaseEntity
import com.hamidrezabashiri.calendar.data.mapper.toDomainModel
import com.hamidrezabashiri.calendar.data.mapper.toEntity
import com.hamidrezabashiri.calendar.data.source.local.CalendarEventLocalDataSource
import com.hamidrezabashiri.calendar.data.source.remote.CalendarEventRemoteDataSource
import com.hamidrezabashiri.calendar.data.util.Result
import com.hamidrezabashiri.calendar.domain.model.CalendarEventModel
import com.hamidrezabashiri.calendar.domain.repository.CalendarEventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

class CalendarEventRepositoryImpl(
    private val localDataSource: CalendarEventLocalDataSource,
    private val remoteDataSource: CalendarEventRemoteDataSource
) : CalendarEventRepository {

    override suspend fun insertEvent(event: CalendarEventModel) {
        localDataSource.insertEvent(event.toEntity())
//        remoteDataSource.syncUserEvent(event.toEntity())
    }

    override suspend fun updateEvent(event: CalendarEventModel) {
        localDataSource.updateEvent(event.toEntity())
        remoteDataSource.syncUserEvent(event.toEntity())
    }

    override suspend fun deleteEvent(eventId: String) {
        localDataSource.deleteEvent(eventId)
    }

    override suspend fun getEventById(eventId: String): CalendarEventModel? =
        localDataSource.getEventById(eventId)?.toDomainModel()

    override fun getAllEvents(): Flow<List<CalendarEventModel>> =
        localDataSource.getAllEvents().map { events ->
            events.map { it.toDomainModel() }
        }

    override fun getEventsByDate(date: String): Flow<List<CalendarEventModel>> =
        localDataSource.getEventsByDate(date).map { events ->
            events.map { it.toDomainModel() }
        }

    override fun getEventsForDateRange(
        startDate: String,
        endDate: String
    ): Flow<List<CalendarEventModel>> =
        localDataSource.getEventsForDateRange(startDate, endDate).map { events ->
            events.map { it.toDomainModel() }
        }

    override suspend fun syncWithRemote(countryCode: String) {
//        val remoteEvents = remoteDataSource.fetchCountrySpecificEvents(countryCode)
//        remoteEvents.forEach { localDataSource.insertEvent(it) }
    }

    override suspend fun fetchHolidaysFromRemote(
        countryCode: String,
        year: Int
    ) {
        // Step 1: Check if the data already exists in the local database
        val (startDate, endDate) = getDateRangeForYear(year)
        // Fetch events from the local data source for the date range (using first to collect the data)
        val cachedEvents = localDataSource.getEventsForDateRange(startDate, endDate).first()

        // Step 2: If events are found in the local database, do not fetch from the remote source
        if (cachedEvents.isNotEmpty()) {
            // If data exists in the database, log or return the cached data
            println("Data found in database for $countryCode and year $year")
            // Optionally, return the cached data if you want to use it here
            return
        }

         when (val remoteHolidaysResponse = remoteDataSource.fetchCountrySpecificHolidaysByYear(countryCode, year)) {
            is Result.Success -> {
                // Map the successful result to domain models
                println(remoteHolidaysResponse.data)
                remoteHolidaysResponse.data.forEach {
                    localDataSource.insertEvent(it.toDatabaseEntity())
                }
            }
            is Result.Error -> {
                // Handle the error case, return an empty list
                println("Error fetching holidays: ${remoteHolidaysResponse.error}")
            }
        }
    }
    fun getDateRangeForYear(year: Int): Pair<String, String> {
        // Create the start date of the year (January 1st)
        val startDate = LocalDate(year, 1, 1)

        // Create the end date of the year (December 31st)
        val endDate = LocalDate(year, 12, 31)

        // Convert to ISO8601 string format (e.g., 2024-01-01)
        return Pair(startDate.toString(), endDate.toString())
    }
}

