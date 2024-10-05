package cdu278.mangotest.auth.state

import android.content.Context
import androidx.datastore.core.DataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthStateStore

@Module
@InstallIn(SingletonComponent::class)
class AuthStateModule {

    @Provides
    @AuthStateStore
    fun provideAuthUserStore(@ApplicationContext context: Context): DataStore<AuthState> {
        return context.authStateDataStore
    }
}