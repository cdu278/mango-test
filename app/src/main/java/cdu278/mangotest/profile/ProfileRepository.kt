package cdu278.mangotest.profile

import cdu278.mangotest.http.ValidatedRequestError
import cdu278.mangotest.op.Op
import cdu278.mangotest.op.fold
import cdu278.mangotest.op.map
import cdu278.mangotest.profile.datasource.local.LocalProfile.Absent
import cdu278.mangotest.profile.datasource.local.LocalProfile.Present
import cdu278.mangotest.profile.datasource.local.LocalProfileDataSource
import cdu278.mangotest.profile.datasource.local.current
import cdu278.mangotest.profile.datasource.remote.RemoteProfileDataSource
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val localDataSource: LocalProfileDataSource,
    private val remoteDataSource: RemoteProfileDataSource,
) {

    private val refreshSignalFlow = MutableStateFlow(System.currentTimeMillis())

    val flow: Flow<OfflineBasedProfile>
        get() = channelFlow {
            var updating: Job? = null
            refreshSignalFlow.collect {
                updating?.cancel()
                updating = launch {
                    if (localDataSource.current() is Absent) {
                        send(OfflineBasedProfile.Syncing)
                        remoteDataSource.get().fold(
                            ifSuccess = { localDataSource.set(it) },
                            ifFailure = { error ->
                                send(OfflineBasedProfile.FailedToSync(error))
                            }
                        )
                    }
                    localDataSource.flow.collect { local ->
                        (local as? Present)?.profile
                            ?.let { send(OfflineBasedProfile.Synced(it)) }
                    }
                }
            }
        }

    fun refresh() {
        refreshSignalFlow.value = System.currentTimeMillis()
    }

    fun update(updated: UpdatedProfile): Op<*, ValidatedRequestError> {
        return remoteDataSource.update(updated)
            .map { updatedAvatars ->
                localDataSource.update {
                    it.copy(
                        name = updated.name,
                        city = updated.city,
                        birthday = updated.birthday,
                        status = updated.status,
                        avatars = updatedAvatars ?: it.avatars,
                    )
                }
            }
    }
}