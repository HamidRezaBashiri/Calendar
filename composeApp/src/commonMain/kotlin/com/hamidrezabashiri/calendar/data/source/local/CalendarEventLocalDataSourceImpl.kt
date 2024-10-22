package com.hamidrezabashiri.calendar.data.source.local


import com.hamidrezabashiri.calendar.data.entity.CalendarEventEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CalendarEventLocalDataSourceImpl(
    private val realm: Realm // Injected Realm instance
) : CalendarEventLocalDataSource {

    // Insert a calendar event
    override suspend fun insertEvent(event: CalendarEventEntity) {
        realm.write {
            copyToRealm(event)
        }
    }

    // Update a calendar event
    override suspend fun updateEvent(event: CalendarEventEntity) {
        realm.write {
            query<CalendarEventEntity>("id == $0", event.id).first().find()?.apply {
                title = event.title
                description = event.description
                startDate = event.startDate
                endDate = event.endDate
                startTime = event.startTime
                endTime = event.endTime
                location = event.location
                isRecurring = event.isRecurring
                isReminderSet = event.isReminderSet
                reminderTimeAndDate = event.reminderTimeAndDate
                category = event.category
            }
        }
    }

    // Delete a calendar event by ID
    override suspend fun deleteEvent(eventId: String) {
        realm.write {
            val event = query<CalendarEventEntity>("id == $0", eventId).first().find()
            if (event != null) {
                delete(event)
            }
        }
    }

    // Get a calendar event by ID
    override suspend fun getEventById(eventId: String): CalendarEventEntity? {
        return realm.query<CalendarEventEntity>("id == $0", eventId).first().find()
    }

    // Get all calendar events as a Flow
    override fun getAllEvents(): Flow<List<CalendarEventEntity>> {
        return realm.query<CalendarEventEntity>().asFlow()
            .map { resultsChange: ResultsChange<CalendarEventEntity> ->
                resultsChange.list // Returns a list of events
            }
    }

    // Get events by a specific date (as Flow)
    override fun getEventsByDate(date: String): Flow<List<CalendarEventEntity>> {
        return realm.query<CalendarEventEntity>("startDate == $0", date).asFlow()
            .map { resultsChange: ResultsChange<CalendarEventEntity> ->
                resultsChange.list // Returns the filtered list of events
            }
    }

    // Get events for a specific date range
    override fun getEventsForDateRange(startDate: String, endDate: String): Flow<List<CalendarEventEntity>> {
        return realm.query<CalendarEventEntity>("startDate >= $0 AND startDate <= $1", startDate, endDate).asFlow()
            .map { resultsChange: ResultsChange<CalendarEventEntity> ->
                resultsChange.list
            }
    }

}