package com.hamidrezabashiri.calendar


import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.NavigationRail
import androidx.compose.material.NavigationRailItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.hamidrezabashiri.calendar.presentation.navigation.BaseTab
import com.hamidrezabashiri.calendar.presentation.navigation.tabs.CalendarTab
import com.hamidrezabashiri.calendar.presentation.navigation.tabs.LibraryTab
import com.hamidrezabashiri.calendar.presentation.navigation.tabs.SettingsTab
import com.hamidrezabashiri.calendar.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme {
        TabNavigationContent()
    }
}

@Composable
fun TabNavigationContent() {
    TabNavigator(CalendarTab()) { // Start with CalendarTab
        val tabNavigator = LocalTabNavigator.current

        Scaffold(
//            drawerContent = { NavigationRailContent(tabNavigator) },
            bottomBar = {
                BottomNavigation {
                    BottomNavigationItem(
                        icon = { Icon(Icons.Default.DateRange, contentDescription = CalendarTab().options.title) },
                        label = { Text(CalendarTab().options.title) },
                        selected = tabNavigator.current == CalendarTab(),
                        onClick = { tabNavigator.current = CalendarTab() }
                    )
                    BottomNavigationItem(
                        icon = { Icon(Icons.Default.Place, contentDescription = LibraryTab().options.title) },
                        label = { Text(LibraryTab().options.title) },
                        selected = tabNavigator.current == LibraryTab(),
                        onClick = { tabNavigator.current = LibraryTab() }
                    )
                    BottomNavigationItem(
                        icon = { Icon(Icons.Default.Settings, contentDescription = LibraryTab().options.title) },
                        label = { Text(SettingsTab().options.title) },
                        selected = tabNavigator.current == SettingsTab(),
                        onClick = { tabNavigator.current = SettingsTab() }
                    )
                }
            }
        ) {
            CurrentTab()
        }
    }
}

@Composable
private fun NavigationRailContent(
    tabNavigator: TabNavigator
) {
    NavigationRail {
        NavigationRailItem(
            selected = tabNavigator.current == CalendarTab(),
            onClick = { tabNavigator.current = CalendarTab() },
            icon = { Icon(Icons.Default.Home, contentDescription = CalendarTab().options.title) },
            label = { Text(CalendarTab().options.title) }
        )
        NavigationRailItem(
            selected = tabNavigator.current == LibraryTab(),
            onClick = { tabNavigator.current = LibraryTab() },
            icon = { Icon(Icons.Default.Settings, contentDescription = LibraryTab().options.title) },
            label = { Text(LibraryTab().options.title) }
        )
        NavigationRailItem(
            selected = tabNavigator.current == SettingsTab(),
            onClick = { tabNavigator.current = SettingsTab() },
            icon = { Icon(Icons.Default.Person, contentDescription = SettingsTab().options.title) },
            label = { Text(SettingsTab().options.title) }
        )
    }
}

@Composable
private fun TabNavigationItem(
    tab: BaseTab,
    tabNavigator: TabNavigator
) {
    Button(onClick = { tabNavigator.current = tab }){
        Text(tab.options.title)
    }



}
