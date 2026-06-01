package com.example.projectmap.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Login : Route

    @Serializable
    data object Register : Route

    @Serializable
    data object PinSetup : Route

    @Serializable
    data object PinVerify : Route

    @Serializable
    data object Dashboard : Route

    @Serializable
    data object Analytics : Route

    @Serializable
    data object Settings : Route
}
