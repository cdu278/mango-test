package cdu278.mangotest.ui.auth.signin

sealed interface SignInEvent {

    data object Authorized : SignInEvent

    class SignUpNeeded(val phone: String) : SignInEvent
}