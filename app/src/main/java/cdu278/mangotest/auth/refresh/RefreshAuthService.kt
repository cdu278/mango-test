package cdu278.mangotest.auth.refresh

import androidx.datastore.core.DataStore
import cdu278.mangotest.auth.state.AuthState
import cdu278.mangotest.auth.state.AuthStateStore
import cdu278.mangotest.auth.state.AuthTokens
import cdu278.mangotest.http.ExecuteHttpStatement
import cdu278.mangotest.http.client.AuthHttpClient
import cdu278.mangotest.op.map
import cdu278.mangotest.op.resultOrNull
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class RefreshAuthService @Inject constructor(
    @AuthHttpClient
    private val httpClient: HttpClient,
    @AuthStateStore
    private val authStateStore: DataStore<AuthState>,
) {

    suspend fun refresh(refreshToken: String): AuthTokens? {
        return ExecuteHttpStatement(
            httpClient.preparePost("refresh-token/") {
                setBody(hashMapOf("refresh_token" to refreshToken))
                contentType(ContentType.Application.Json)
            }
        ).map {
            val tokens = it.body<AuthTokens>()
            authStateStore.updateData { AuthState.Authorized(tokens) }
            tokens
        }.resultOrNull()
    }
}