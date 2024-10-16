package com.hamidrezabashiri.calendar.data.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.datetime.LocalDateTime

class CalendarEvent : RealmObject {
    @PrimaryKey
    var id: String = ""
    var title: String = ""
    var description: String = ""
    var startDate: String = "" // Store as ISO8601 string
    var endDate: String = "" // Store as ISO8601 string
    var isAllDay: Boolean = false
    var location: String = ""
    var isRecurring: Boolean = false
    var isReminderSet: Boolean = false
    var reminderTimeAndDate: String = "" // Store as ISO8601 string

}