package cdu278.mangotest.http

import io.ktor.client.statement.HttpResponse

sealed interface HttpError {

    class Io(val e: Exception) : HttpError

    class Failure(val response: HttpResponse) : HttpError
}

val HttpError.Failure.httpCode: Int
    get() = response.status.value