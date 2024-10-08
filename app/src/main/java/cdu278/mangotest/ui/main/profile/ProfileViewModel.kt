package cdu278.mangotest.ui.main.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cdu278.mangotest.http.HttpError
import cdu278.mangotest.image.ImageBase64Converter
import cdu278.mangotest.op.error.asDialogs
import cdu278.mangotest.op.error.collectingErrors
import cdu278.mangotest.op.loadingAware
import cdu278.mangotest.profile.OfflineFirstProfile.SyncStatus.Failed
import cdu278.mangotest.profile.OfflineFirstProfile.SyncStatus.Synchronizing
import cdu278.mangotest.profile.ProfileRepository
import cdu278.mangotest.profile.UpdatedProfile
import cdu278.mangotest.profile.avatar.AvatarUrlFactory
import cdu278.mangotest.session.UserSession
import cdu278.mangotest.ui.error.request.RequestErrorDialogUi
import cdu278.mangotest.ui.main.profile.avatar.AvatarUi
import cdu278.mangotest.ui.main.profile.dialog.ProfileDialogUi
import cdu278.mangotest.ui.uiSharingStarted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val avatarUrlFactory: AvatarUrlFactory,
    private val imageBase64Converter: ImageBase64Converter,
    private val userSession: UserSession,
) : ViewModel() {

    private val inputFlow = MutableStateFlow(ProfileInput())

    private val loadingFlow = MutableStateFlow(false)

    private val _errorDialogFlow = MutableStateFlow<RequestErrorDialogUi?>(null)
    val errorDialogFlow: StateFlow<RequestErrorDialogUi?>
        get() = _errorDialogFlow

    private val _dialogFlow = MutableStateFlow<ProfileDialogUi?>(null)
    val dialogFlow: StateFlow<ProfileDialogUi?>
        get() = _dialogFlow

    private val profileFlow =
        repository.flow.shareIn(viewModelScope, uiSharingStarted, replay = 1)

    val modelFlow: StateFlow<ProfileUi?> =
        combine(
            inputFlow,
            profileFlow,
            loadingFlow,
        ) { input, model, loading ->
            ProfileUi(
                data = model.profile?.let { profile ->
                    val error = input.validate()
                    ProfileUi.Data(
                        avatar = input.avatarUri?.let { AvatarUi.Local(it) }
                            ?: profile.avatars?.miniAvatarPath
                                ?.let { AvatarUi.Remote(avatarUrlFactory.create(path = it)) },
                        username = profile.username,
                        phone = "+${profile.phone}",
                        name = input.name ?: profile.name,
                        city = input.city ?: profile.city ?: "",
                        birthday = input.birthday ?: profile.birthday,
                        status = input.status ?: profile.status ?: "",
                        canSave = input.edited && error == null,
                        error = error,
                    )
                },
                loading = loading || model.syncStatus is Synchronizing,
                syncFailure = (model.syncStatus as? Failed)?.error?.asSyncFailure(),
            )
        }.stateIn(viewModelScope, uiSharingStarted, initialValue = null)

    private fun ProfileInput.validate(): ProfileErrorUi? {
        name?.let { if (it.isBlank()) return ProfileErrorUi.EmptyName }
        return null
    }

    private fun HttpError.asSyncFailure(): ProfileUi.SyncFailure {
        return when (this) {
            is HttpError.Io -> ProfileUi.SyncFailure.ConnectionError
            else -> ProfileUi.SyncFailure.UnknownError
        }
    }

    fun refresh() {
        repository.refresh()
    }

    fun inputName(value: String) {
        inputFlow.update { it.copy(name = value) }
    }

    fun inputCity(value: String) {
        inputFlow.update { it.copy(city = value) }
    }

    fun inputStatus(value: String) {
        inputFlow.update { it.copy(status = value) }
    }

    fun pickBirthday() {
        _dialogFlow.value = ProfileDialogUi.PickBirthday
    }

    fun inputBirthday(dateMillis: Long?) {
        if (dateMillis != null) {
            inputFlow.update {
                it.copy(
                    birthday = Instant.fromEpochMilliseconds(dateMillis)
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                        .date
                )
            }
        }
    }

    fun inputAvatar(uri: Uri) {
        inputFlow.update { it.copy(avatarUri = uri) }
    }

    private suspend fun ProfileInput.asUpdatedProfile(): UpdatedProfile {
        val profile = profileFlow.first().profile!!
        return UpdatedProfile(
            username = profile.username,
            name = this.name?.trim() ?: profile.name,
            city = if (this.city != null) {
                this.city.takeIf { it.isNotBlank() }?.trim()
            } else {
                profile.city
            },
            birthday = this.birthday ?: profile.birthday,
            status = if (this.status != null) {
                this.status.takeIf { it.isNotBlank() }?.trim()
            } else {
                profile.status
            },
            avatar = this.avatarUri?.let { uri ->
                UpdatedProfile.Avatar(
                    filename = uri.path!!,
                    base64 = imageBase64Converter.convert(uri),
                )
            },
        )
    }

    fun save() {
        viewModelScope.launch {
            repository.update(inputFlow.value.asUpdatedProfile())
                .loadingAware(loadingFlow)
                .perform(collectingErrors(asDialogs(_errorDialogFlow)) {
                    inputFlow.value = ProfileInput()
                })
        }
    }

    fun logOut() {
        _dialogFlow.value = ProfileDialogUi.Logout
    }

    fun confirmLogOut() {
        viewModelScope.launch {
            userSession.terminate()
        }
    }

    fun dismissDialog() {
        _errorDialogFlow.value = null
        _dialogFlow.value = null
    }
}