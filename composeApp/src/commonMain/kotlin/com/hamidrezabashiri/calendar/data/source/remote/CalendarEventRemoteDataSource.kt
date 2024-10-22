package com.hamidrezabashiri.calendar.data.source.remote

import com.hamidrezabashiri.calendar.data.entity.CalendarEventEntity

interface CalendarEventRemoteDataSource {
    suspend fun fetchCountrySpecificEvents(countryCode: String): List<CalendarEventEntity>
    suspend fun syncUserEvent(event: CalendarEventEntity)
}