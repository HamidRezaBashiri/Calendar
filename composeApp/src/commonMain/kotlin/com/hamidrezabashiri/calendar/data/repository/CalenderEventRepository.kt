package com.hamidrezabashiri.calendar.data.repository


import com.hamidrezabashiri.calendar.data.model.CalendarEvent
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime

class CalendarEventRepository(private val realm: Realm) {
    fun getAllEvents(): Flow<List<CalendarEvent>> {
        return realm.query<CalendarEvent>().asFlow().map { it.list }
    }

    suspend fun addEvent(event: CalendarEvent) {
        realm.write {
            copyToRealm(event)
        }
    }

    suspend fun updateEvent(event: CalendarEvent) {
        realm.write {
            val existingEvent = query<CalendarEvent>("id == $0", event.id).first().find()
            existingEvent?.apply {
                title = event.title
                description = event.description
                startDate = event.startDate
                endDate = event.endDate
                isAllDay = event.isAllDay
            }
        }
    }

    suspend fun deleteEvent(id: String) {
        realm.write {
            val event = query<CalendarEvent>("id == $0", id).first().find()
            event?.let { delete(it) }
        }
    }

    fun getEventsForDateRange(start: LocalDateTime, end: LocalDateTime): Flow<List<CalendarEvent>> {
        return realm.query<CalendarEvent>("startDate >= $0 AND endDate <= $1", start.toString(), end.toString())
            .asFlow()
            .map { it.list }
    }
}