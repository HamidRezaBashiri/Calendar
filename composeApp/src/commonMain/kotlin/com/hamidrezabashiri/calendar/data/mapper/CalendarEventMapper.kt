package com.hamidrezabashiri.calendar.data.mapper

import com.hamidrezabashiri.calendar.data.entity.CalendarEventEntity
import com.hamidrezabashiri.calendar.domain.model.CalendarEventModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
// Extension function to convert CalendarEventEntity to CalendarEvent
fun CalendarEventEntity.toDomainModel(): CalendarEventModel {
    return CalendarEventModel(
        id = id,
        title = title,
        description = description,
        startDate = LocalDate.parse(startDate),
        endDate = LocalDate.parse(endDate),
        startTime = LocalTime.parse(startTime),
        endTime = LocalTime.parse(endTime),
        location = location,
        isRecurring = isRecurring,
        isReminderSet = isReminderSet,
        reminderTimeAndDate = reminderTimeAndDate?.let { LocalDateTime.parse(it) },
        category = category
    )
}

// Extension function to convert CalendarEvent to CalendarEventEntity
fun CalendarEventModel.toEntity(): CalendarEventEntity {
    return CalendarEventEntity().apply {
        id = this@toEntity.id
        title = this@toEntity.title
        description = this@toEntity.description
        startDate = this@toEntity.startDate.toString()
        endDate = this@toEntity.endDate.toString()
        startTime = this@toEntity.startTime.toString()
        endTime = this@toEntity.endTime.toString()
        location = this@toEntity.location
        isRecurring = this@toEntity.isRecurring
        isReminderSet = this@toEntity.isReminderSet
        reminderTimeAndDate = this@toEntity.reminderTimeAndDate?.toString()
        category = this@toEntity.category
    }
}

// Extension function for List of entities
fun List<CalendarEventEntity>.toDomainModels(): List<CalendarEventModel> = map { it.toDomainModel() }

// Extension function for List of domain models
fun List<CalendarEventModel>.toEntities(): List<CalendarEventEntity> = map { it.toEntity() }

// Nullable extension functions for safe conversions
fun CalendarEventEntity?.toDomainModelOrNull(): CalendarEventModel? = this?.toDomainModel()

fun CalendarEventModel?.toEntityOrNull(): CalendarEventEntity? = this?.toEntity()