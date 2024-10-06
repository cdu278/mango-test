package cdu278.mangotest.profile

import android.content.Context
import androidx.datastore.core.DataStore
import cdu278.mangotest.profile.datasource.local.LocalProfile
import cdu278.mangotest.profile.datasource.local.localProfileDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocalProfileStore

@Module
@InstallIn(ViewModelComponent::class)
class ProfileModule {

    @Provides
    @LocalProfileStore
    fun provideLocalProfileDataStore(
        @ApplicationContext context: Context
    ): DataStore<LocalProfile> {
        return context.localProfileDataStore
    }
}