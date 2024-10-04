package cdu278.mangotest.auth.refresh

import cdu278.mangotest.auth.tokens.AuthTokens
import cdu278.mangotest.http.ExecuteHttpStatement
import cdu278.mangotest.http.client.AuthHttpClient
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
) {

    suspend fun refresh(refreshToken: String): AuthTokens? {
        return ExecuteHttpStatement(
            httpClient.preparePost("refresh-token") {
                setBody(hashMapOf("refresh_token" to refreshToken))
                contentType(ContentType.Application.Json)
            }
        ).resultOrNull()?.body()
    }
}