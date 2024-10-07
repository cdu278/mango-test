package cdu278.mangotest.phone

import com.google.i18n.phonenumbers.AsYouTypeFormatter

class PhoneCountry(
    val code: String,
    val phoneCode: String,
    formatter: () -> AsYouTypeFormatter,
) {

    val formatter: AsYouTypeFormatter by lazy(formatter)
}