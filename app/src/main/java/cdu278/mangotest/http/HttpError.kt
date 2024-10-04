package cdu278.mangotest.http

sealed interface HttpError {

    class Io(val e: Exception) : HttpError

    class Failure(val httpCode: Int) : HttpError
}