//package com.hamidrezabashiri.calendar.presentation.screens.home
//
//import androidx.compose.runtime.Composable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material.AlertDialog
//import androidx.compose.material.Button
//import androidx.compose.material.Icon
//import androidx.compose.material.IconButton
//import androidx.compose.material.Text
//import androidx.compose.material.TextField
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Home
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import cafe.adriel.voyager.core.screen.Screen
//import cafe.adriel.voyager.kodein.rememberScreenModel
//import com.hamidrezabashiri.calendar.data.model.CalendarEvent
//import kotlinx.datetime.Clock
//import kotlinx.datetime.TimeZone
//import kotlinx.datetime.toLocalDateTime
//import java.util.*
//
//class HomeScreen : Screen {
//
//    @Composable
//    override fun Content() {
////        val screenModel = rememberScreenModel<HomeContract>()
////        val state by screenModel.state.collectAsState()
//
//        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//            when (val currentState = state) {
////                is HomeContract.State.Initial -> Text("Initializing...")
////                is HomeContract.State.Loading -> CircularProgressIndicator()
////                is HomeContract.State.Content -> ContentView(currentState.events, screenModel::onEvent)
////                is HomeContract.State.Error -> Text("Error: ${currentState.message}")
//            }
//        }
//    }
//
//    @Composable
//    private fun ContentView(events: List<CalendarEvent>, onEvent: (HomeContract.Event) -> Unit) {
//        var showAddEventDialog by remember { mutableStateOf(false) }
//
//        Column {
//            Button(
//                onClick = { showAddEventDialog = true },
//                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
//            ) {
//                Text("Add New Event")
//            }
//            LazyColumn {
//                items(events) { event ->
//                    EventItem(event, onDelete = { onEvent(HomeContract.Event.DeleteEvent(event.id)) })
//                }
//            }
//        }
//
//        if (showAddEventDialog) {
//            AddEventDialog(
//                onAddEvent = { event ->
//                    onEvent(HomeContract.Event.AddEvent(event))
//                    showAddEventDialog = false
//                },
//                onDismiss = { showAddEventDialog = false }
//            )
//        }
//    }
//
//    @Composable
//    private fun EventItem(event: CalendarEvent, onDelete: () -> Unit) {
//        Card(
//            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
//        ) {
//            Row(
//                modifier = Modifier.padding(16.dp),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Column {
//                    Text(event.title, style = MaterialTheme.typography.titleMedium)
//                    Text(event.startDate, style = MaterialTheme.typography.bodyMedium)
//                }
//                IconButton(onClick = onDelete) {
//                    Icon(Icons.Default.Home, contentDescription = "Delete event")
//                }
//            }
//        }
//    }
//
//    @Composable
//    private fun AddEventDialog(onAddEvent: (CalendarEvent) -> Unit, onDismiss: () -> Unit) {
//        var title by remember { mutableStateOf("") }
//        var description by remember { mutableStateOf("") }
//
//        AlertDialog(
//            onDismissRequest = onDismiss,
//            title = { Text("Add New Event") },
//            text = {
//                Column {
//                    TextField(
//                        value = title,
//                        onValueChange = { title = it },
//                        label = { Text("Title") },
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    TextField(
//                        value = description,
//                        onValueChange = { description = it },
//                        label = { Text("Description") },
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                }
//            },
//            confirmButton = {
//                Button(
//                    onClick = {
//                        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
//                        val event = CalendarEvent().apply {
//                            id = UUID.randomUUID().toString()
//                            this.title = title
//                            this.description = description
//                            startDate = now.toString()
//                            endDate = now.toString()
//                        }
//                        onAddEvent(event)
//                    }
//                ) {
//                    Text("Add")
//                }
//            },
//            dismissButton = {
//                Button(onClick = onDismiss) {
//                    Text("Cancel")
//                }
//            }
//        )
//    }
//}