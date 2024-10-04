package cdu278.mangotest.ui.root

import kotlinx.serialization.Serializable

interface Destinations {

    @Serializable
    data object Auth {

        @Serializable
        data object SignIn

        @Serializable
        class SignUp(val phoneNumber: String)
    }

    @Serializable
    data object Main
}