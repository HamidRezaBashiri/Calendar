package com.hamidrezabashiri.calendar.presentation.screens.addEvent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hamidrezabashiri.calendar.domain.model.CalendarEventModel

@Composable
fun EventDetailsForm(
    event: CalendarEventModel,
    onEventChange: (CalendarEventModel) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
    ) {
        Text(text = "Add New Event", style = MaterialTheme.typography.h6)

        Spacer(modifier = Modifier.height(8.dp))

        // Title input
        OutlinedTextField(
            value = event.title,
            onValueChange = { onEventChange(event.copy(title = it)) },
            label = { Text("Title") }
        )

        // Description input
        OutlinedTextField(
            value = event.description,
            onValueChange = { onEventChange(event.copy(description = it)) },
            label = { Text("Description") }
        )

        // Location input
        OutlinedTextField(
            value = event.location,
            onValueChange = { onEventChange(event.copy(location = it)) },
            label = { Text("Location") }
        )

        // Start and End Date Pickers (assuming separate composables are created for date and time pickers)
        Text(text = "Start Date: ${event.startDate}")
        Text(text = "End Date: ${event.endDate}")

        // Start and End Time Pickers
        Text(text = "Start Time: ${event.startTime}")
        Text(text = "End Time: ${event.endTime}")

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onCancel) { Text("Cancel") }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onSave) { Text("Save") }
        }
    }
}