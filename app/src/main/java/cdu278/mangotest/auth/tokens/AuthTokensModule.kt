package cdu278.mangotest.auth.tokens

import android.content.Context
import androidx.datastore.core.DataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AuthTokensModule {

    @Provides
    @AuthUserStore
    fun provideAuthUserStore(@ApplicationContext context: Context): DataStore<AuthUser?> {
        return context.authUserDataStore
    }
}