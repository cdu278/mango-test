package cdu278.mangotest.ui.auth.signin

sealed interface SignInInput {

    data object Phone : SignInInput

    data class Code(
        val code: String = "",
    ) : SignInInput
}