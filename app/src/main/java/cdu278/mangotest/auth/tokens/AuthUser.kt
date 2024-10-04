package cdu278.mangotest.auth.tokens

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthUser(
    val tokens: AuthTokens?,
    @SerialName("is_user_exists")
    val exists: Boolean,
    val phone: String?,
)

val AuthUser.accessToken: String
    get() = tokens!!.accessToken

val AuthUser.refreshToken: String
    get() = tokens!!.refreshToken