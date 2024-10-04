package cdu278.mangotest.ui.auth.phone

import cdu278.mangotest.auth.PhoneNumber
import cdu278.mangotest.ui.uiSharingStarted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class PhoneViewModel(
    coroutineScope: CoroutineScope,
    private val onChange: (PhoneNumber) -> Unit,
) {

    private val _phoneNumberFlow = MutableStateFlow("")
    val phoneNumberFlow: StateFlow<String>
        get() = _phoneNumberFlow

    init {
        phoneNumberFlow.onEach {
            if (validate(it)) {
                onChange(PhoneNumber.Valid(it))
            } else {
                onChange(PhoneNumber.NotValid)
            }
        }.launchIn(coroutineScope)
    }

    private fun validate(phone: String): Boolean {
        return phone.startsWith('+') && phone.length == 12
    }

    val isValidFlow: StateFlow<Boolean> =
        phoneNumberFlow.map {
            it.startsWith('+') && it.length == 11
        }.stateIn(coroutineScope, uiSharingStarted, initialValue = false)

    fun input(value: String) {
        _phoneNumberFlow.value = value
    }

    class Factory @Inject constructor() {

        fun create(
            coroutineScope: CoroutineScope,
            onChange: (PhoneNumber) -> Unit,
        ): PhoneViewModel {
            return PhoneViewModel(
                coroutineScope,
                onChange,
            )
        }
    }
}