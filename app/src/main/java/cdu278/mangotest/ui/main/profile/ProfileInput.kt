package cdu278.mangotest.ui.main.profile

import android.net.Uri
import kotlinx.datetime.LocalDate

data class ProfileInput(
    val avatarUri: Uri? = null,
    val name: String? = null,
    val birthday: LocalDate? = null,
    val city: String? = null,
    val status: String? = null,
) {

    val edited: Boolean
        get() = avatarUri != null ||
                name != null ||
                birthday != null ||
                city != null ||
                status != null
}