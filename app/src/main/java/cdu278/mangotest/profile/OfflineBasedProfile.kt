package cdu278.mangotest.profile

import cdu278.mangotest.http.HttpError

sealed interface OfflineBasedProfile {

    data object Syncing : OfflineBasedProfile

    class Synced(val profile: Profile) : OfflineBasedProfile

    class FailedToSync(val error: HttpError) : OfflineBasedProfile
}