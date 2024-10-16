package com.hamidrezabashiri.calendar


import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.hamidrezabashiri.calendar.di.appModule
import org.kodein.di.DI
import org.kodein.di.compose.withDI

@Composable
@Preview
fun App() {
    MaterialTheme {
        val di = DI {
            import(appModule)
        }

        withDI(di) {
            Navigator(HomeScreen()) { navigator ->
                SlideTransition(navigator)
            }
        }
    }
}