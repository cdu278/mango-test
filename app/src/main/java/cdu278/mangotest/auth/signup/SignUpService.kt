package cdu278.mangotest.auth.signup

import androidx.datastore.core.DataStore
import cdu278.mangotest.auth.tokens.AuthTokens
import cdu278.mangotest.auth.tokens.AuthUser
import cdu278.mangotest.auth.tokens.AuthUserStore
import cdu278.mangotest.http.ExecuteHttpStatement
import cdu278.mangotest.http.ValidatedRequestError
import cdu278.mangotest.http.client.AuthHttpClient
import cdu278.mangotest.op.Op
import cdu278.mangotest.op.map
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SignUpService @Inject constructor(
    @AuthHttpClient
    private val httpClient: HttpClient,
    @AuthUserStore
    private val authUserStore: DataStore<AuthUser>,
) {

    fun signUp(name: String, username: String): Op<*, ValidatedRequestError> {
        return ExecuteHttpStatement {
            httpClient.preparePost("register/") {
                setBody(hashMapOf(
                    "phone" to authUserStore.data.first().phone,
                    "name" to name,
                    "username" to username,
                ))
                contentType(ContentType.Application.Json)
            }
        }.map(
            transform = { response ->
                val tokens = response.body<AuthTokens>()
                authUserStore.updateData { it.copy(tokens = tokens, exists = true, phone = null) }
            },
            transformError = { ValidatedRequestError.create(it) },
        )
    }
}