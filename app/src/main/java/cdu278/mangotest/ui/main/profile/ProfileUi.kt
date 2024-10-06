package cdu278.mangotest.ui.main.profile

import cdu278.mangotest.ui.main.profile.avatar.AvatarUi
import cdu278.mangotest.zodiac.ZodiacSign
import kotlinx.datetime.LocalDate

data class ProfileUi(
    val data: Data?,
    val loading: Boolean,
    val syncFailure: SyncFailure?,
) {

    data class Data(
        val avatar: AvatarUi?,
        val username: String,
        val phone: String,
        val name: String,
        val city: String,
        val birthday: LocalDate?,
        val status: String,
        val canSave: Boolean,
        val error: ProfileErrorUi?,
    ) {

        val zodiacSign: ZodiacSign?
            get() = if (birthday != null) {
                ZodiacSign.entries.find { birthday in it.interval }!!
            } else {
                null
            }
    }

    enum class SyncFailure {

        ConnectionError,
        UnknownError,
    }
}