package cdu278.mangotest.auth.signin

import androidx.datastore.core.DataStore
import cdu278.mangotest.auth.tokens.AuthTokens
import cdu278.mangotest.auth.tokens.AuthUser
import cdu278.mangotest.auth.tokens.AuthUserStore
import cdu278.mangotest.http.ExecuteHttpStatement
import cdu278.mangotest.http.HttpError
import cdu278.mangotest.http.ValidatedRequestError
import cdu278.mangotest.http.client.AuthHttpClient
import cdu278.mangotest.op.Op
import cdu278.mangotest.op.map
import cdu278.mangotest.op.mapError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.inject.Inject

class SignInService @Inject constructor(
    @AuthHttpClient
    private val httpClient: HttpClient,
    @AuthUserStore
    private val authUserStore: DataStore<AuthUser?>,
) {

    fun sendAuthCode(phone: String): Op<*, ValidatedRequestError> {
        return ExecuteHttpStatement {
            httpClient.preparePost("send-auth-code/") {
                setBody(hashMapOf("phone" to phone))
                contentType(ContentType.Application.Json)
            }
        }.mapError(ValidatedRequestError.Companion::create)
    }

    fun checkAuthCode(phone: String, code: String): Op<Boolean, CheckAuthCodeError> {
        return ExecuteHttpStatement {
            httpClient.preparePost("check-auth-code/") {
                setBody(hashMapOf(
                    "phone" to phone,
                    "code" to code,
                ))
                contentType(ContentType.Application.Json)
            }
        }.map(
            transform = {
                val response = it.body<CheckResponse>()
                authUserStore.updateData {
                    AuthUser(
                        response.tokens,
                        response.userExists,
                        phone = phone.takeUnless { response.userExists },
                    )
                }
                response.userExists
            },
            transformError = { error ->
                if (error is HttpError.Failure && error.httpCode == 404) {
                    CheckAuthCodeError.InvalidCode
                } else {
                    CheckAuthCodeError.Wrapped(ValidatedRequestError.create(error))
                }
            }
        )
    }

    @Serializable
    private class CheckResponse(
        @SerialName("access_token")
        val accessToken: String?,
        @SerialName("refresh_token")
        val refreshToken: String?,
        @SerialName("is_user_exists")
        val userExists: Boolean,
    ) {

        val tokens: AuthTokens?
            get() = if (accessToken != null && refreshToken != null) {
                AuthTokens(accessToken, refreshToken)
            } else {
                null
            }
    }
}