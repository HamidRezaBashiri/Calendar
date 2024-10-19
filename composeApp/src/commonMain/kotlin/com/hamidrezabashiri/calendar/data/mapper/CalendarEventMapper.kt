package com.hamidrezabashiri.calendar.data.mapper

import com.hamidrezabashiri.calendar.data.entity.CalendarEventEntity
import com.hamidrezabashiri.calendar.domain.model.CalendarEvent


fun CalendarEventEntity.toDomainModel(): CalendarEvent {
    return CalendarEvent(
        id = id,
        title = title,
        startTime = startDate,
        endTime = endDate,
    )
}

//fun CalendarEvent.toCalendarEntity(): CalendarEventEntity {
//    return CalendarEventEntity().apply {
//        id = this@toCalendarEntity.id
//        title = this@toCalendarEntity.title
//        startDate = this@toCalendarEntity.startTime // Assuming the startTime is already in ISO8601 string format
//        endDate = this@toCalendarEntity.endTime     // Assuming the endTime is already in ISO8601 string format
//        // You can add more default or dummy values for the other fields if necessary
//        description = "" // Provide a default or mapped value
//        isAllDay = false // Adjust based on your logic
//        location = "" // Provide a default or mapped value
//        isRecurring = false // Adjust based on your logic
//        isReminderSet = false // Adjust based on your logic
//        reminderTimeAndDate = "" // Provide a default or mapped value
//    }
//}