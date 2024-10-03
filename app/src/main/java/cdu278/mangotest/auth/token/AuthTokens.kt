package cdu278.mangotest.auth.token

import kotlinx.serialization.Serializable

@Serializable
class AuthTokens(
    val token: String,
    val refreshToken: String,
)