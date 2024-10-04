package cdu278.mangotest.ui.auth.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cdu278.mangotest.auth.PhoneNumber
import cdu278.mangotest.auth.signin.CheckAuthCodeError
import cdu278.mangotest.auth.signin.CheckAuthCodeError.InvalidCode
import cdu278.mangotest.auth.signin.SignInService
import cdu278.mangotest.op.fold
import cdu278.mangotest.op.loadingAware
import cdu278.mangotest.ui.auth.phone.PhoneViewModel
import cdu278.mangotest.ui.auth.signin.SignInInput.Code
import cdu278.mangotest.ui.auth.signin.SignInInput.Phone
import cdu278.mangotest.ui.error.request.RequestErrorDialogUi
import cdu278.mangotest.ui.uiSharingStarted
import dagger.hilt.android.lifecycle.HiltViewModel
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
class SignInViewModel @Inject constructor(
    private val signInService: SignInService,
    phoneViewModelFactory: PhoneViewModel.Factory,
) : ViewModel() {

    private val phoneFlow = MutableStateFlow<PhoneNumber>(PhoneNumber.NotValid)

    private val inputFlow = MutableStateFlow<SignInInput>(Phone)

    private val loadingFlow = MutableStateFlow(false)

    private val dialogFlow = MutableStateFlow<SignInDialogUi?>(null)

    val modelFlow: StateFlow<SignInUi> =
        combine(
            phoneFlow,
            inputFlow,
            loadingFlow,
            dialogFlow,
        ) { phone, input, loading, dialog ->
            SignInUi(
                state = when (input) {
                    is Phone -> SignInUi.State.PhoneInput
                    is Code -> SignInUi.State.CodeInput(code = input.code)
                },
                isValid = when (input) {
                    is Phone -> phone is PhoneNumber.Valid
                    is Code -> input.code.let { it.length == 6 && it.all(Char::isDigit) }
                },
                loading = loading,
                dialog = dialog,
            )
        }.stateIn(viewModelScope, uiSharingStarted, initialValue = SignInUi())

    private val _eventFlow = MutableSharedFlow<SignInEvent>()
    val eventFlow: Flow<SignInEvent>
        get() = _eventFlow

    val phoneViewModel =
        phoneViewModelFactory.create(
            viewModelScope,
            onChange = {
                phoneFlow.value = it
                inputFlow.value = Phone
            },
        )

    fun inputCode(code: String) {
        inputFlow.update { (it as Code).copy(code = code) }
    }

    private val phone
        get() = (phoneFlow.value as PhoneNumber.Valid).value

    fun sendCode() {
        viewModelScope.launch {
            signInService.sendAuthCode(phone)
                .loadingAware(loadingFlow)
                .fold(
                    ifSuccess = { inputFlow.value = Code() },
                    ifFailure = {
                        dialogFlow.value =
                            SignInDialogUi.RequestError(RequestErrorDialogUi.create(it))
                    }
                )
        }
    }

    fun checkCode() {
        viewModelScope.launch {
            val code = (inputFlow.value as Code).code
            signInService.checkAuthCode(phone, code)
                .loadingAware(loadingFlow)
                .fold(
                    ifSuccess = { userExists ->
                        _eventFlow.emit(
                            if (userExists) {
                                SignInEvent.Authorized
                            } else {
                                SignInEvent.SignUpNeeded
                            }
                        )
                    },
                    ifFailure = { error ->
                        dialogFlow.value =
                            when (error) {
                                is InvalidCode -> SignInDialogUi.InvalidCode
                                is CheckAuthCodeError.Wrapped ->
                                    SignInDialogUi.RequestError(
                                        RequestErrorDialogUi.create(error.error)
                                    )
                            }
                    }
                )
        }
    }

    fun dismissDialog() {
        dialogFlow.value = null
    }
}