package cdu278.mangotest.ui.error.request

import cdu278.mangotest.http.HttpError
import cdu278.mangotest.http.ValidatedRequestError

sealed interface RequestErrorDialogUi {

    data class ValidationError(val message: String) : RequestErrorDialogUi

    data object ConnectionError : RequestErrorDialogUi

    data object UnknownError : RequestErrorDialogUi

    companion object {

        fun create(error: ValidatedRequestError): RequestErrorDialogUi {
            return when (error) {
                is ValidatedRequestError.Validation -> ValidationError(error.message)
                is ValidatedRequestError.Http -> create(error.error)
            }
        }

        fun create(error: HttpError): RequestErrorDialogUi {
            return when (error) {
                is HttpError.Io -> ConnectionError
                is HttpError.Failure -> UnknownError
            }
        }
    }
}