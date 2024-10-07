package cdu278.mangotest.phone

import com.google.i18n.phonenumbers.PhoneNumberUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class PhoneModule {

    @Provides
    fun providePhoneNumberUtil(): PhoneNumberUtil {
        return PhoneNumberUtil.getInstance()
    }
}