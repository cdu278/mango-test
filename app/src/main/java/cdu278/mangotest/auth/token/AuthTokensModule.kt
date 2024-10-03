package cdu278.mangotest.auth.token

import android.content.Context
import androidx.datastore.core.DataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityRetainedComponent::class)
class AuthTokensModule {

    @Provides
    @AuthTokensStore
    fun provideDataStore(@ApplicationContext context: Context): DataStore<AuthTokens?> {
        return context.authTokensDataStore
    }
}