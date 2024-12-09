package com.hamidrezabashiri.calendar.data.source.remote

import com.hamidrezabashiri.calendar.data.entity.CalendarEventEntity
import com.hamidrezabashiri.calendar.data.entity.HolidayEntity
import com.hamidrezabashiri.calendar.data.util.NetworkError
import com.hamidrezabashiri.calendar.data.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException

class CalendarEventRemoteDataSourceImpl(private val httpClient: HttpClient,private val networkConfig: NetworkConfig):CalendarEventRemoteDataSource {

    override suspend fun fetchCountrySpecificHolidaysByYear(
        countryCode: String,
        year: Int
    ): Result<List<HolidayEntity> ,NetworkError> {
       val response = try {
           httpClient.get("https://api.api-ninjas.com/v1/holidays") {
               parameter("country", countryCode)
               parameter("year", year)
               header("X-Api-Key", networkConfig.apiKey)
           }
       }catch(e: UnresolvedAddressException) {
           return Result.Error(NetworkError.NO_INTERNET)
       } catch(e: SerializationException) {
           return Result.Error(NetworkError.SERIALIZATION)
       }
        return when(response.status.value){
            in 200..299 -> {
                val holidayEntityList  = response.body<List<HolidayEntity>>()
                Result.Success(holidayEntityList)
            }
            401 -> Result.Error(NetworkError.UNAUTHORIZED)
            409 -> Result.Error(NetworkError.CONFLICT)
            408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
            413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
            in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
            else -> Result.Error(NetworkError.UNKNOWN)
        }
    }

    override suspend fun syncUserEvent(event: CalendarEventEntity) {
        TODO("Not yet implemented")
    }

}