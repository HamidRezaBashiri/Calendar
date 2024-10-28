package com.hamidrezabashiri.calendar.presentation.navigation.tabs

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.hamidrezabashiri.calendar.presentation.navigation.BaseTab
import com.hamidrezabashiri.calendar.presentation.screens.settings.SettingScreen
import compose.icons.Octicons
import compose.icons.octicons.Gear16

class SettingsTab : BaseTab() {
    override val options: TabOptions
        @Composable
        get() = TabOptions(
            index = 2u,
            title = "Settings",
            icon = rememberVectorPainter(Octicons.Gear16)
        )

    @Composable
    override fun Content() {
        SettingScreen()
    }
}
