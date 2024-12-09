package com.hamidrezabashiri.calendar.domain.usecase

import com.hamidrezabashiri.calendar.domain.usecase.event.AddEventUseCase
import com.hamidrezabashiri.calendar.domain.usecase.event.DeleteEventUseCase
import com.hamidrezabashiri.calendar.domain.usecase.event.FetchHolidaysFromNetworkUseCase
import com.hamidrezabashiri.calendar.domain.usecase.event.GetEventDetailsUseCase
import com.hamidrezabashiri.calendar.domain.usecase.event.GetEventsForDateUseCase
import com.hamidrezabashiri.calendar.domain.usecase.event.GetEventsForMonthUseCase
import com.hamidrezabashiri.calendar.domain.usecase.event.UpdateEventUseCase

data class CalendarUseCases(
    val getEventsForDate: GetEventsForDateUseCase,
    val getEventsForMonth: GetEventsForMonthUseCase,
    val addEvent: AddEventUseCase,
    val updateEvent: UpdateEventUseCase,
    val deleteEvent: DeleteEventUseCase,
    val getEventDetails: GetEventDetailsUseCase,
    val fetchHolidaysFromNetworkUseCase: FetchHolidaysFromNetworkUseCase
)