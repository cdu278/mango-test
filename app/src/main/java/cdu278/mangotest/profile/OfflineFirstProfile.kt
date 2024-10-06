package cdu278.mangotest.profile

import cdu278.mangotest.http.HttpError

class OfflineFirstProfile(
    val profile: Profile?,
    val syncStatus: SyncStatus,
) {

    sealed interface SyncStatus {

        data object Synchronizing : SyncStatus

        data object Synchronized : SyncStatus

        class Failed(val error: HttpError) : SyncStatus
    }
}