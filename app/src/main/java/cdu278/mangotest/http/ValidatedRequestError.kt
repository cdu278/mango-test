package cdu278.mangotest.http

sealed interface ValidatedRequestError {

    class Validation(val message: String) : ValidatedRequestError

    class Http(val error: HttpError) : ValidatedRequestError
}