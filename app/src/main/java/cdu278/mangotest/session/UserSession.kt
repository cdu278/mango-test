package cdu278.mangotest.session

import androidx.datastore.core.DataStore
import cdu278.mangotest.auth.state.AuthState
import cdu278.mangotest.auth.state.AuthStateStore
import cdu278.mangotest.http.client.MainHttpClient
import cdu278.mangotest.profile.datasource.local.LocalProfileDataSource
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin
import javax.inject.Inject

class UserSession @Inject constructor(
    @AuthStateStore
    private val authStateStore: DataStore<AuthState>,
    private val localProfileDataSource: LocalProfileDataSource,
    @MainHttpClient
    private val httpClient: HttpClient,
) {

    suspend fun terminate() {
        localProfileDataSource.clear()

        httpClient.plugin(Auth)
            .providers.filterIsInstance<BearerAuthProvider>()
            .firstOrNull()?.clearToken()

        authStateStore.updateData { AuthState.NotAuthorized }
    }
}