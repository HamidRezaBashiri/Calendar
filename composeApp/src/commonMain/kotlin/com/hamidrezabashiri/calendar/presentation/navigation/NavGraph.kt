package com.hamidrezabashiri.calendar.presentation.navigation

import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.Navigator

object NavGraph {
    const val HOME_ROUTE = "home"
    const val DETAIL_ROUTE = "detail"

    fun setupGraph() {
        ScreenRegistry {
//            register(HOME_ROUTE) { HomeScreen() }
//            register(DETAIL_ROUTE) { DetailScreen(it.get<String>("userId")) }
        }
    }
}

inline fun Navigator.pushScreen(route: String, crossinline params: () -> Map<String, Any> = { emptyMap() }) {
//    push(ScreenRegistry.get(route, params()))
}