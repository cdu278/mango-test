package cdu278.mangotest.profile.datasource.local

import androidx.datastore.core.DataStore
import cdu278.mangotest.profile.LocalProfileStore
import cdu278.mangotest.profile.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class LocalProfileDataSource @Inject constructor(
    @LocalProfileStore
    private val dataStore: DataStore<LocalProfile>,
) {

    val flow: Flow<LocalProfile>
        get() = dataStore.data

    suspend fun set(profile: Profile) {
        dataStore.updateData { LocalProfile.Present(profile) }
    }

    suspend fun update(transform: (Profile) -> Profile) {
        dataStore.updateData {
            val current = (it as LocalProfile.Present).profile
            LocalProfile.Present(transform(current))
        }
    }

    suspend fun clear() {
        dataStore.updateData { LocalProfile.Absent }
    }
}

suspend fun LocalProfileDataSource.current(): LocalProfile = flow.first()