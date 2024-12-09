package com.hamidrezabashiri.calendar.data.mapper

import com.hamidrezabashiri.calendar.data.entity.CalendarEventEntity
import com.hamidrezabashiri.calendar.data.entity.HolidayEntity

fun HolidayEntity.toDatabaseEntity(): CalendarEventEntity {
    return CalendarEventEntity().apply {
        id = "$iso-$date" // Unique identifier combining ISO code and date
        title = name // Use holiday name as the title
        description = "Holiday in $country" // Provide a default description
        startDate = date // Set startDate from the holiday's date
        endDate = date // Assume holidays are one-day events
        startTime = "00:00:00" // Default start time (midnight)
        endTime = "23:59:59" // Default end time (end of the day)
        location = country // Set the country as the location
        isRecurring = false // Holidays are generally not recurring in this context
        isReminderSet = false // Default no reminder for holidays
        reminderTimeAndDate = null // No reminder time set
        category = eventType // Use event type as the category
    }
}
