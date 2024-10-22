package com.hamidrezabashiri.calendar.domain.usecase.event


import com.hamidrezabashiri.calendar.domain.model.CalendarEvent
import com.hamidrezabashiri.calendar.domain.repository.CalendarEventRepository
import com.hamidrezabashiri.calendar.domain.util.Result

class UpdateEventUseCase(
    private val repository: CalendarEventRepository
) {
    suspend operator fun invoke(event: CalendarEvent): Result<Unit> {
        return try {
            repository.updateEvent(event)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
