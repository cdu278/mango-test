package cdu278.mangotest.ui.auth.phone

import cdu278.mangotest.ui.auth.phone.country.PhoneCountryViewModel
import com.google.i18n.phonenumbers.AsYouTypeFormatter

data class PhoneUi(
    val flagResId: Int? = null,
    val isValid: Boolean = false,
    val formatter: AsYouTypeFormatter? = null,
    val countryPicker: CountryPicker? = null,
) {

    data class CountryPicker(val viewModel: PhoneCountryViewModel)
}