package cdu278.mangotest.ui.auth.signup

import cdu278.mangotest.ui.error.request.RequestErrorDialogUi

data class SignUpUi(
    val name: String = "",
    val username: String = "",
    val error: Error? = null,
    val loading: Boolean = false,
    val dialog: RequestErrorDialogUi? = null,
) {

    enum class Error {

        EmptyName,
        EmptyUsername,
        InvalidUsername,
    }
}