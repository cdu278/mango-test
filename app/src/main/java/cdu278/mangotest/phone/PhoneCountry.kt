package cdu278.mangotest.phone

import com.google.i18n.phonenumbers.AsYouTypeFormatter

class PhoneCountry(
    val code: String,
    val phoneCode: String,
    name: () -> String,
    formatter: () -> AsYouTypeFormatter,
) {

    val name: String by lazy(name)

    val formatter: AsYouTypeFormatter by lazy(formatter)
}