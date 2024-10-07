package cdu278.mangotest.ui.auth.phone.country

import cdu278.mangotest.phone.PhoneCountry
import kotlinx.coroutines.CoroutineScope

class PhoneCountryViewModel(
    coroutineScope: CoroutineScope,
    private val countries: List<PhoneCountry>,
    private val onCountryPicked: (PhoneCountry) -> Unit,
    val dismiss: () -> Unit,
) {
}