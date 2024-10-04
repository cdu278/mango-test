package cdu278.mangotest.auth.tokens

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class AuthTokens(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String,
)