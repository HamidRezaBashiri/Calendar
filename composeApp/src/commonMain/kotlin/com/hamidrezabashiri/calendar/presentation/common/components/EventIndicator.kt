package com.hamidrezabashiri.calendar.presentation.common.components

import androidx.compose.ui.graphics.Color
import com.hamidrezabashiri.calendar.domain.model.CalendarEventModel

data class EventIndicator(
    val type: String,
    val color: Color
) {
    companion object {
        val PUBLIC_HOLIDAY = EventIndicator("PUBLIC_HOLIDAY", Color.Magenta)
        val OBSERVANCE = EventIndicator("OBSERVANCE", Color.Green)
        val SEASON = EventIndicator("SEASON", Color.Cyan)
    }
}
