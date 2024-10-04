package cdu278.mangotest.ui.error.request

import cdu278.mangotest.http.HttpError
import cdu278.mangotest.http.ValidatedRequestError

enum class RequestErrorDialogUi {

    ValidationError,
    ConnectionError,
    UnknownError;

    companion object {

        fun create(error: ValidatedRequestError): RequestErrorDialogUi {
            return when (error) {
                is ValidatedRequestError.Validation -> ValidationError
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