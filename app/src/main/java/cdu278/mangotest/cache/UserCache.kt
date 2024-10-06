package cdu278.mangotest.cache

import cdu278.mangotest.http.client.MainHttpClient
import cdu278.mangotest.profile.datasource.local.LocalProfileDataSource
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin
import javax.inject.Inject

class UserCache @Inject constructor(
    private val localProfileDataSource: LocalProfileDataSource,
    @MainHttpClient
    private val httpClient: HttpClient,
) {

    suspend fun clear() {
        localProfileDataSource.clear()
        httpClient.plugin(Auth)
            .providers.filterIsInstance<BearerAuthProvider>()
            .firstOrNull()?.clearToken()
    }
}