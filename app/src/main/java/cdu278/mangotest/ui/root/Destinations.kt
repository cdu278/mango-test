package cdu278.mangotest.ui.root

import kotlinx.serialization.Serializable

interface Destinations {

    @Serializable
    data object Auth {

        @Serializable
        data object SignIn

        @Serializable
        data object SignUp
    }

    @Serializable
    data object Main

    @Serializable
    class Chat(val id: Int)
}