package cdu278.mangotest.phone

import android.content.Context
import com.google.i18n.phonenumbers.PhoneNumberUtil
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
        return withContext(Dispatchers.Default) {
            phoneNumberUtil.supportedRegions
                .map { countryCode ->
                    PhoneCountry(
                        code = countryCode,
                        phoneCode = phoneNumberUtil.getCountryCodeForRegion(countryCode).toString(),
                        name = { Locale("", countryCode).displayCountry },
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