package com.hamidrezabashiri.calendar.presentation.navigation.tabs

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.hamidrezabashiri.calendar.presentation.navigation.BaseTab
import com.hamidrezabashiri.calendar.presentation.screens.settings.SettingScreen

class SettingsTab : BaseTab() {
    override val options: TabOptions
        @Composable
        get() = TabOptions(
            index = 2u,
            title = "Settings",
//            icon = Icons.Default.Settings
        )

    @Composable
    override fun Content() {
        SettingScreen()
    }
}
