package cdu278.mangotest.ui.main.profile.avatar

import android.net.Uri

sealed interface AvatarUi {

    data class Local(val uri: Uri) : AvatarUi

    data class Remote(val url: String) : AvatarUi
}