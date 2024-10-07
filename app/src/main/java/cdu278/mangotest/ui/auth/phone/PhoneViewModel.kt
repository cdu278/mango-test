package cdu278.mangotest.ui.auth.phone

import cdu278.mangotest.auth.PhoneNumber
import cdu278.mangotest.phone.PhoneCountry
import cdu278.mangotest.phone.PhoneCountryService
import cdu278.mangotest.ui.auth.phone.country.PhoneCountryViewModel
import cdu278.mangotest.ui.uiSharingStarted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class PhoneViewModel(
    private val coroutineScope: CoroutineScope,
    private val onChange: (PhoneNumber) -> Unit,
    private val phoneService: PhoneCountryService,
) {

    private val _phoneFlow = MutableStateFlow("")
    val phoneFlow: StateFlow<String>
        get() = _phoneFlow

    private val defaultCountryCode = phoneService.defaultCountryCode

    private val countriesFlow =
        flow { emit(phoneService.countries()) }
            .shareIn(coroutineScope, uiSharingStarted, replay = 1)

    private suspend fun countries() = countriesFlow.first()

    init {
        coroutineScope.launch(Dispatchers.Default) {
            countries().find { it.code == defaultCountryCode }
                ?.let { _phoneFlow.value = "+${it.phoneCode}" }
        }
    }

    private val countryViewModelFlow = MutableStateFlow<PhoneCountryViewModel?>(null)

    private val pickedCountryFlow = MutableStateFlow<PhoneCountry?>(null)

    val modelFlow: StateFlow<PhoneUi> =
        combine(
            phoneFlow,
            countryViewModelFlow,
            pickedCountryFlow
        ) { inputPhone, countryViewModel, pickedCountry ->
            val phone = inputPhone.withLeadingPlus()
            val country = currentCountry(pickedCountry, phone)
            PhoneUi(
                flagResId = country?.let { phoneService.countryFlag(it.code) },
                isValid = country?.let { phoneService.validate(phone, it.code) } == true,
                formatter = country?.formatter,
                countryPicker = countryViewModel?.let(PhoneUi::CountryPicker),
            )
        }.flowOn(Dispatchers.Default)
            .stateIn(coroutineScope, uiSharingStarted, initialValue = PhoneUi())

    private suspend fun currentCountry(
        pickedCountry: PhoneCountry?,
        phone: String,
    ): PhoneCountry? {
        return pickedCountry
            ?: countries().filter { phone.startsWith("+${it.phoneCode}") }
                .takeIf { it.isNotEmpty() }
                ?.let { countries ->
                    countries.find { it.code == defaultCountryCode }
                        ?: countries.first()
                }
    }

    private fun String.withLeadingPlus(): String {
        return this.takeIf { it.startsWith('+') } ?: "+$this"
    }

    init {
        _phoneFlow.onEach { inputPhone ->
            val phone = inputPhone.withLeadingPlus()
            val country = currentCountry(pickedCountryFlow.value, phone)
            if (country?.let { phoneService.validate(phone, it.code) } == true) {
                onChange(PhoneNumber.Valid(phone))
            } else {
                onChange(PhoneNumber.NotValid)
            }
        }.launchIn(coroutineScope)
    }

    fun input(value: String) {
        val cleanedPhone =
            value.mapIndexedNotNull { index, c ->
                c.takeIf { (it == '+' && index == 0) || c.isDigit() }
            }.toCharArray().let(::String)
        _phoneFlow.value = cleanedPhone
    }

    fun chooseCountry() {
        coroutineScope.launch {
            countryViewModelFlow.value =
                PhoneCountryViewModel(
                    coroutineScope,
                    countries(),
                    onCountryPicked = { },
                    dismiss = { countryViewModelFlow.value = null },
                )
        }
    }

    class Factory @Inject constructor(
        private val phoneService: PhoneCountryService,
    ) {

        fun create(
            coroutineScope: CoroutineScope,
            onChange: (PhoneNumber) -> Unit,
        ): PhoneViewModel {
            return PhoneViewModel(
                coroutineScope,
                onChange,
                phoneService,
            )
        }
    }
}