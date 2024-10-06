package cdu278.mangotest.profile.datasource.local

import cdu278.mangotest.profile.Profile
import kotlinx.serialization.Serializable

@Serializable
sealed interface LocalProfile {

    @Serializable
    data object Absent : LocalProfile

    @Serializable
    class Present(val profile: Profile) : LocalProfile
}