package cdu278.mangotest.ui.auth.signup

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cdu278.mangotest.auth.signup.SignUpService
import cdu278.mangotest.auth.state.AuthState
import cdu278.mangotest.auth.state.AuthStateStore
import cdu278.mangotest.datastore.value
import cdu278.mangotest.op.loadingAware
import cdu278.mangotest.ui.error.request.RequestErrorDialogUi
import cdu278.mangotest.ui.error.request.collectingError
import cdu278.mangotest.ui.uiSharingStarted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpService: SignUpService,
    @AuthStateStore
    private val authStateStore: DataStore<AuthState>,
) : ViewModel() {

    private val inputFlow = MutableStateFlow(SignUpInput())

    private val loadingFlow = MutableStateFlow(false)

    private val dialogFlow = MutableStateFlow<RequestErrorDialogUi?>(null)

    private val _eventFlow = MutableSharedFlow<SignUpEvent>()
    val eventFlow: Flow<SignUpEvent>
        get() = _eventFlow

    private val phone =
        viewModelScope.async {
            (authStateStore.value() as AuthState.SignUpRequired).phone
        }

    val modelFlow: StateFlow<SignUpUi> =
        combine(
            inputFlow,
            loadingFlow,
            dialogFlow,
        ) { input, loading, dialog ->
            SignUpUi(
                phone = phone.await(),
                name = input.name,
                username = input.username,
                error = when {
                    input.name.isBlank() -> SignUpUi.Error.EmptyName
                    input.username.isBlank() -> SignUpUi.Error.EmptyUsername
                    input.username.trim().any { !it.isAllowedUsernameChar() } ->
                        SignUpUi.Error.InvalidUsername
                    else -> null
                },
                loading = loading,
                dialog = dialog,
            )
        }.stateIn(viewModelScope, uiSharingStarted, initialValue = SignUpUi())

    private fun Char.isAllowedUsernameChar(): Boolean {
        return this.isLetter() || this.isDigit() || this == '_' || this == '-'
    }

    fun inputName(value: String) {
        inputFlow.update { it.copy(name = value) }
    }

    fun inputUsername(value: String) {
        inputFlow.update { it.copy(username = value) }
    }

    fun signUp() {
        viewModelScope.launch {
            val input = inputFlow.value
            signUpService.signUp(name = input.name.trim(), username = input.username.trim())
                .loadingAware(loadingFlow)
                .perform(collectingError(dialogFlow) {
                    _eventFlow.emit(SignUpEvent.SignedUp)
                })
        }
    }

    fun dismissDialog() {
        dialogFlow.value = null
    }
}