package cdu278.mangotest.ui.auth.signin

data class SignInUi(
    val state: State = State.PhoneInput,
    val isValid: Boolean = false,
    val loading: Boolean = false,
    val dialog: SignInDialogUi? = null,
) {

    sealed interface State {

        data object PhoneInput : State

        data class CodeInput(
            val code: String,
        ) : State
    }
}