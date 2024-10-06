package cdu278.mangotest.profile

import cdu278.mangotest.http.ValidatedRequestError
import cdu278.mangotest.op.Op
import cdu278.mangotest.op.fold
import cdu278.mangotest.op.map
import cdu278.mangotest.profile.OfflineFirstProfile.SyncStatus
import cdu278.mangotest.profile.datasource.local.LocalProfile
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

    val flow: Flow<OfflineFirstProfile>
        get() = channelFlow {
            var updating: Job? = null
            refreshSignalFlow.collect {
                updating?.cancel()

                val localProfile = (localDataSource.current() as? LocalProfile.Present)?.profile
                send(OfflineFirstProfile(localProfile, SyncStatus.Synchronizing))

                updating = launch {
                    remoteDataSource.get()
                        .fold(
                            ifSuccess = { remoteProfile ->
                                localDataSource.set(remoteProfile)
                                localDataSource.flow.collect collectLocal@ {
                                    val profile =
                                        (it as? LocalProfile.Present)?.profile
                                            ?: return@collectLocal
                                    send(OfflineFirstProfile(profile, SyncStatus.Synchronized))
                                }
                            },
                            ifFailure = { error ->
                                send(OfflineFirstProfile(localProfile, SyncStatus.Failed(error)))
                            }
                        )
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