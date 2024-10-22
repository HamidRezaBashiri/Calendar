package com.hamidrezabashiri.calendar.domain.usecase.event


import com.hamidrezabashiri.calendar.domain.model.CalendarEvent
import com.hamidrezabashiri.calendar.domain.repository.CalendarEventRepository
import com.hamidrezabashiri.calendar.domain.util.Result

class GetEventDetailsUseCase(
    private val repository: CalendarEventRepository
) {
    suspend operator fun invoke(eventId: String): Result<CalendarEvent> {
        return try {
            val event = repository.getEventById(eventId)
            if (event != null) {
                Result.Success(event)
            } else {
                Result.Error(Exception("Event not found"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}