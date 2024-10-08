package cdu278.mangotest.auth.signup

import androidx.datastore.core.DataStore
import cdu278.mangotest.auth.state.AuthState
import cdu278.mangotest.auth.state.AuthStateStore
import cdu278.mangotest.datastore.value
import cdu278.mangotest.http.ExecuteHttpStatement
import cdu278.mangotest.http.ParseValidatedRequestErrorStrategy
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
import javax.inject.Inject

class SignUpService @Inject constructor(
    @AuthHttpClient
    private val httpClient: HttpClient,
    @AuthStateStore
    private val authStateStore: DataStore<AuthState>,
    private val parseErrorStrategy: ParseValidatedRequestErrorStrategy,
) {

    fun signUp(name: String, username: String): Op<*, ValidatedRequestError> {
        return ExecuteHttpStatement {
            httpClient.preparePost("register/") {
                val authState = authStateStore.value() as AuthState.SignUpRequired
                setBody(hashMapOf(
                    "phone" to authState.phone,
                    "name" to name,
                    "username" to username,
                ))
                contentType(ContentType.Application.Json)
            }
        }.map(
            transform = { response ->
                authStateStore.updateData {
                    AuthState.Authorized(tokens = response.body())
                }
            },
            transformError = parseErrorStrategy::apply,
        )
    }
}