package cdu278.mangotest.profile

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UpdatedProfile(
    val username: String,
    val name: String,
    val city: String?,
    @Serializable(with = BirthdaySerializer::class)
    val birthday: LocalDate?,
    val status: String?,
    val avatar: Avatar? = null,
) {

    @Serializable
    class Avatar(
        val filename: String,
        @SerialName("base_64")
        val base64: String,
    )
}