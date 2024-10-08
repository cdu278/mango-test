package cdu278.mangotest.http

import io.ktor.client.call.body
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.inject.Inject

class ParseValidatedRequestErrorStrategy @Inject constructor() {

    suspend fun apply(error: HttpError): ValidatedRequestError {
        return if (error is HttpError.Failure) {
            val response = error.response
            when (error.httpCode) {
                422 -> {
                    val detail = response.body<Error422>().details.first()
                    val fieldName = detail.loc[1]
                    return ValidatedRequestError.Validation(
                        message = buildString {
                            append("Invalid "); append(fieldName); append(". ")
                            append(detail.msg.replaceFirstChar(Char::uppercase))
                        }
                    )
                }
                400 -> ValidatedRequestError.Validation(response.body<Error400>().detail.message)
                else -> ValidatedRequestError.Http(error)
            }
        } else {
            ValidatedRequestError.Http(error)
        }
    }

    @Serializable
    private class Error422(
        @SerialName("detail")
        val details: List<Detail>,
    ) {

        @Serializable
        class Detail(
            val loc: List<String>,
            val msg: String,
        )
    }

    @Serializable
    private class Error400(
        val detail: Detail,
    ) {

        @Serializable
        class Detail(
            val message: String,
        )
    }
}