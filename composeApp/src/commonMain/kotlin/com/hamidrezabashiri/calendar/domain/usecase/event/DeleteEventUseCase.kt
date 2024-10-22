package com.hamidrezabashiri.calendar.domain.usecase.event


import com.hamidrezabashiri.calendar.domain.repository.CalendarEventRepository
import com.hamidrezabashiri.calendar.domain.util.Result

class DeleteEventUseCase(
    private val repository: CalendarEventRepository
) {
    suspend operator fun invoke(eventId: String): Result<Unit> {
        return try {
            repository.deleteEvent(eventId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}