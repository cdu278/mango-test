package cdu278.mangotest.profile.datasource.remote

import cdu278.mangotest.http.ExecuteHttpStatement
import cdu278.mangotest.http.HttpError
import cdu278.mangotest.http.ParseValidatedRequestErrorStrategy
import cdu278.mangotest.http.ValidatedRequestError
import cdu278.mangotest.http.client.MainHttpClient
import cdu278.mangotest.op.Op
import cdu278.mangotest.op.map
import cdu278.mangotest.profile.Profile
import cdu278.mangotest.profile.avatar.ProfileAvatars
import cdu278.mangotest.profile.UpdatedProfile
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.prepareGet
import io.ktor.client.request.preparePut
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.inject.Inject

class RemoteProfileDataSource @Inject constructor(
    @MainHttpClient
    private val httpClient: HttpClient,
    private val parseErrorStrategy: ParseValidatedRequestErrorStrategy,
) {

    fun get(): Op<Profile, HttpError> {
        return ExecuteHttpStatement {
            httpClient.prepareGet("me/")
        }.map {
            it.body<RemoteProfile>().profile
        }
    }

    @Serializable
    private class RemoteProfile(
        @SerialName("profile_data")
        val profile: Profile,
    )

    fun update(profile: UpdatedProfile): Op<ProfileAvatars?, ValidatedRequestError> {
        return ExecuteHttpStatement {
            httpClient.preparePut("me/") {
                setBody(profile)
                contentType(ContentType.Application.Json)
            }
        }.map(
            transform = { it.body<UpdateResponse>().avatars },
            transformError = parseErrorStrategy::apply
        )
    }

    @Serializable
    private class UpdateResponse(
        val avatars: ProfileAvatars? = null,
    )
}