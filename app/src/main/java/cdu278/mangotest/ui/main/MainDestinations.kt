package cdu278.mangotest.ui.main

import kotlinx.serialization.Serializable

interface MainDestinations {

    @Serializable
    data object Chats

    @Serializable
    data object Profile
}