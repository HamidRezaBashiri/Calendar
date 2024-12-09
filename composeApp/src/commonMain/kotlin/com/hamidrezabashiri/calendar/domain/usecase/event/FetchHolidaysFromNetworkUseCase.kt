package com.hamidrezabashiri.calendar.domain.usecase.event

import com.hamidrezabashiri.calendar.domain.repository.CalendarEventRepository

class FetchHolidaysFromNetworkUseCase(
    private val repository: CalendarEventRepository
) {

    /**
     * Fetches holidays from the network for a given country and year and stores them in the database.
     *
     * @param countryCode The country code for which to fetch holidays.
     * @param year The year for which to fetch holidays.
     */
    suspend fun execute(countryCode: String, year: Int) {
        try {
            repository.fetchHolidaysFromRemote(countryCode, year)
            println("Holidays successfully fetched ")
        } catch (e: Exception) {
            println("Error in FetchHolidaysFromNetworkUseCase: ${e.message}")
        }
    }
}
