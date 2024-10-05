package cdu278.mangotest.auth.state

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class AuthTokens(
    @SerialName("access_token")
    val access: String,
    @SerialName("refresh_token")
    val refresh: String,
)