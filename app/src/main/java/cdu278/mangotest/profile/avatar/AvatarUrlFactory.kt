package cdu278.mangotest.profile.avatar

import cdu278.mangotest.http.client.Hostname
import javax.inject.Inject

class AvatarUrlFactory @Inject constructor(
    @Hostname
    private val hostname: String,
) {

    fun create(path: String): String = "https://$hostname/$path"
}