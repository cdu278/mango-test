package cdu278.mangotest.phone

import android.content.Context
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import com.murgupluoglu.flagkit.FlagKit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

class PhoneCountryService @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val phoneNumberUtil: PhoneNumberUtil,
) {

    val defaultCountryCode: String
        get() = Locale.getDefault().country

    suspend fun countries(): List<PhoneCountry> {
//        return listOf(
//            PhoneCountry(
//                code = "kz",
//                name = "Kazakhstan",
//                phoneCode = "7",
//                phoneLength = 10,
//            ),
//            PhoneCountry(
//                code = "ru",
//                name = "Russia",
//                phoneCode = "7",
//                phoneLength = 10,
//            ),
//            PhoneCountry(
//                code = "us",
//                name = "US",
//                phoneCode = "1",
//                phoneLength = 10,
//            ),
//        )
        return withContext(Dispatchers.Default) {
            phoneNumberUtil.supportedRegions
                .map { countryCode ->
                    PhoneCountry(
                        code = countryCode,
                        phoneCode = phoneNumberUtil.getCountryCodeForRegion(countryCode).toString(),
                        formatter = { phoneNumberUtil.getAsYouTypeFormatter(countryCode) },
                    )
                }
        }
    }

    suspend fun validate(phone: String, countryCode: String): Boolean {
        return withContext(Dispatchers.Default) {
            phoneNumberUtil.isPossibleNumber(phone, countryCode)
        }
    }

    fun countryFlag(countryCode: String): Int {
        return FlagKit.getResId(context, countryCode)
    }
}