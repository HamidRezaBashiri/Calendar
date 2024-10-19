package com.hamidrezabashiri.calendar.data.repository


import com.hamidrezabashiri.calendar.data.entity.CalendarEventEntity
import com.hamidrezabashiri.calendar.data.source.local.RealmDatabase
import com.hamidrezabashiri.calendar.domain.repository.IEventRepository
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId

class EventRepository:IEventRepository {
    private val realm = RealmDatabase.getInstance()

    override fun getAllEvents(): Flow<List<CalendarEventEntity>> {
        return realm.query<CalendarEventEntity>().asFlow().map { it.list }
    }

    override suspend fun addEvent(event: CalendarEventEntity) {
        realm.write {
            copyToRealm(event)
        }
    }

    override suspend fun updateEvent(event: CalendarEventEntity) {
        realm.write {
            val queriedEvent = query<CalendarEventEntity>("id == $0", event.id).first().find()
            queriedEvent?.apply {
                title = event.title
                description = event.description
//                date = event.date
//                isUniversal = event.isUniversal
//                countryCode = event.countryCode
//                language = event.language
            }
        }
    }

    override suspend fun deleteEvent(id: ObjectId) {
        realm.write {
            val event = query<CalendarEventEntity>("id == $0", id).first().find()
            event?.let { delete(it) }
        }
    }

    override fun getEventsByDate(date: Long): Flow<List<CalendarEventEntity>> {
        return realm.query<CalendarEventEntity>("date == $0", date).asFlow().map { it.list }
    }

    override fun getEventsByCountry(countryCode: String): Flow<List<CalendarEventEntity>> {
        return realm.query<CalendarEventEntity>("countryCode == $0", countryCode).asFlow().map { it.list }
    }

    override fun getUniversalEvents(): Flow<List<CalendarEventEntity>> {
        return realm.query<CalendarEventEntity>("isUniversal == true").asFlow().map { it.list }
    }

}


