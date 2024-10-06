package cdu278.mangotest.profile.avatar

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ProfileAvatars(
    @SerialName("miniAvatar")
    val miniAvatarPath: String,
)