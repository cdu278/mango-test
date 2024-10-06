package cdu278.mangotest.profile

import cdu278.mangotest.profile.avatar.ProfileAvatars
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val username: String,
    val phone: String,
    val name: String,
    val city: String? = null,
    val birthday: LocalDate? = null,
    val status: String? = null,
    val avatars: ProfileAvatars? = null,
)