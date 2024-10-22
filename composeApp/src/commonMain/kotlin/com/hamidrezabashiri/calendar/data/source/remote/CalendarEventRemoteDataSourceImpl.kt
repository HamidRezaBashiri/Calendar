package com.hamidrezabashiri.calendar.data.source.remote

import com.hamidrezabashiri.calendar.data.entity.CalendarEventEntity

class CalendarEventRemoteDataSourceImpl():CalendarEventRemoteDataSource {
    override suspend fun fetchCountrySpecificEvents(countryCode: String): List<CalendarEventEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun syncUserEvent(event: CalendarEventEntity) {
        TODO("Not yet implemented")
    }

}