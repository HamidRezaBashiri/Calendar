//package com.hamidrezabashiri.calendar.presentation.screens.home
//
//
//import cafe.adriel.voyager.core.model.StateScreenModel
//import com.hamidrezabashiri.calendar.data.model.CalendarEvent
//import com.hamidrezabashiri.calendar.data.repository.CalendarEventRepository
//import kotlinx.coroutines.flow.collect
//import kotlinx.coroutines.launch
//import org.kodein.di.instance
//import org.kodein.di.DI
//
//class HomeContract(di: DI) : StateScreenModel<HomeContract.State>(State.Initial) {
//    private val repository: CalendarEventRepository by di.instance()
//
//    sealed class State {
//        object Initial : State()
//        data class Content(val events: List<CalendarEvent>) : State()
//        object Loading : State()
//        data class Error(val message: String) : State()
//    }
//
//    sealed class Event {
//        object LoadEvents : Event()
//        data class AddEvent(val event: CalendarEvent) : Event()
//        data class DeleteEvent(val id: String) : Event()
//    }
//
//    init {
//        loadEvents()
//    }
//
//    fun onEvent(event: Event) {
//        when (event) {
//            is Event.LoadEvents -> loadEvents()
//            is Event.AddEvent -> addEvent(event.event)
//            is Event.DeleteEvent -> deleteEvent(event.id)
//        }
//    }
//
//    private fun loadEvents() {
//        coroutineScope.launch {
//            mutableState.value = State.Loading
//            repository.getAllEvents().collect { events ->
//                mutableState.value = State.Content(events)
//            }
//        }
//    }
//
//    private fun addEvent(event: CalendarEvent) {
//        coroutineScope.launch {
//            repository.addEvent(event)
//        }
//    }
//
//    private fun deleteEvent(id: String) {
//        coroutineScope.launch {
//            repository.deleteEvent(id)
//        }
//    }
//}