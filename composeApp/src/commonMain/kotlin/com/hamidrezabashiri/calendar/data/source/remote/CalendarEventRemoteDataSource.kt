package com.hamidrezabashiri.calendar.data.source.remote

import com.hamidrezabashiri.calendar.data.entity.CalendarEventEntity
import com.hamidrezabashiri.calendar.data.entity.HolidayEntity
import com.hamidrezabashiri.calendar.data.util.NetworkError
import com.hamidrezabashiri.calendar.data.util.Result

interface CalendarEventRemoteDataSource {
    suspend fun fetchCountrySpecificHolidaysByYear(countryCode: String,year:Int): Result<List<HolidayEntity> , NetworkError>
    suspend fun syncUserEvent(event: CalendarEventEntity)
}