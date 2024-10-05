package cdu278.mangotest.http.client

import androidx.datastore.core.DataStore
import cdu278.mangotest.auth.state.AuthStateStore
import cdu278.mangotest.auth.refresh.RefreshAuthService
import cdu278.mangotest.auth.state.AuthState
import cdu278.mangotest.datastore.value
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.CIOEngineConfig
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthHttpClient

@Module
@InstallIn(SingletonComponent::class)
class HttpClientModule {

    private fun HttpClientConfig<CIOEngineConfig>.basicConfig(json: Json) {
        defaultRequest {
            url("https://plannerok.ru/api/v1/users/")
        }
        install(ContentNegotiation) {
            json(json)
        }
        install(Logging) {
            level = LogLevel.BODY
        }
    }

    @Provides
    @Singleton
    @AuthHttpClient
    fun provideAuthClient(json: Json): HttpClient {
        return HttpClient(CIO) {
            basicConfig(json)
        }
    }

    @Provides
    @Singleton
    @MainHttpClient
    fun provideHttpClient(
        @AuthStateStore
        authStateStore: DataStore<AuthState>,
        refreshAuthService: RefreshAuthService,
        json: Json,
    ): HttpClient {
        return HttpClient(CIO) {
            basicConfig(json)
            install(Auth) {
                bearer {
                    loadTokens {
                        authStateStore.value()
                            .let { it as? AuthState.Authorized }?.tokens
                            ?.let {
                                BearerTokens(
                                    accessToken = it.access,
                                    refreshToken = it.refresh
                                )
                            }
                    }
                    refreshTokens {
                        refreshAuthService.refresh(oldTokens?.refreshToken!!)
                            ?.let { BearerTokens(it.access, it.refresh) }
                    }
                }
            }
        }
    }
}