package com.hamidrezabashiri.calendar.data.entity

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class CalendarEventEntity : RealmObject {
    @PrimaryKey
    var id: String = ""
    var title: String = ""
    var description: String = ""
    var startDate: String = "" // ISO8601 string
    var endDate: String = "" // ISO8601 string
    var startTime: String = "" // ISO8601 string for time
    var endTime: String = "" // ISO8601 string for time
    var location: String = ""
    var isRecurring: Boolean = false
    var isReminderSet: Boolean = false
    var reminderTimeAndDate: String? = null // ISO8601 string
    var category: String = ""
}