package cdu278.mangotest.ui.auth.signin

import cdu278.mangotest.ui.error.request.RequestErrorDialogUi

sealed interface SignInDialogUi {

    data object InvalidCode : SignInDialogUi

    data class RequestError(val dialogUi: RequestErrorDialogUi) : SignInDialogUi
}