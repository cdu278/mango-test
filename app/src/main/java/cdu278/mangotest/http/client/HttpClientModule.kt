package cdu278.mangotest.http.client

import androidx.datastore.core.DataStore
import cdu278.mangotest.auth.tokens.AuthUserStore
import cdu278.mangotest.auth.refresh.RefreshAuthService
import cdu278.mangotest.auth.tokens.AuthUser
import cdu278.mangotest.auth.tokens.accessToken
import cdu278.mangotest.auth.tokens.refreshToken
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
import kotlinx.coroutines.flow.first
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
        @AuthUserStore
        authUserStore: DataStore<AuthUser?>,
        refreshAuthService: RefreshAuthService,
        json: Json,
    ): HttpClient {
        return HttpClient(CIO) {
            basicConfig(json)
            install(Auth) {
                bearer {
                    loadTokens {
                        authUserStore.data
                            .first()
                            ?.let { BearerTokens(it.accessToken, it.refreshToken) }
                    }
                    refreshTokens {
                        refreshAuthService.refresh(oldTokens?.refreshToken!!)
                            ?.let { BearerTokens(it.accessToken, it.refreshToken) }
                    }
                }
            }
        }
    }
}