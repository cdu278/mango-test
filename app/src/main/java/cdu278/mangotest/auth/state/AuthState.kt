package cdu278.mangotest.auth.state

import kotlinx.serialization.Serializable

@Serializable
sealed interface AuthState {

    @Serializable
    data object NotAuthorized : AuthState

    @Serializable
    class SignUpRequired(val phone: String) : AuthState

    @Serializable
    class Authorized(val tokens: AuthTokens) : AuthState
}