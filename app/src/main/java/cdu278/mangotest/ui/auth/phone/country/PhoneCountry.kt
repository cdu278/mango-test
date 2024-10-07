package cdu278.mangotest.ui.auth.phone.country

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cdu278.mangotest.R

@Composable
fun PhoneCountry(
    country: PhoneCountryUi,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        with(country) {
            Text(
                text = stringResource(R.string.countryCodePicker_countryNameFmt, name, code),
                modifier = Modifier
                    .weight(1f)
            )
            Text(phoneCode)
        }
    }
}