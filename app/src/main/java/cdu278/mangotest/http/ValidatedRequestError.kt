package cdu278.mangotest.http

sealed interface ValidatedRequestError {

    data object Validation : ValidatedRequestError

    class Http(val error: HttpError) : ValidatedRequestError

    companion object {

        fun create(error: HttpError): ValidatedRequestError {
            return if (error is HttpError.Failure && error.httpCode == 422) {
                Validation
            } else {
                Http(error)
            }
        }
    }
}