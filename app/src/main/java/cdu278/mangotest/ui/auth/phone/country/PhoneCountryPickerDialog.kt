package cdu278.mangotest.ui.auth.phone.country

import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneCountryPickerDialog(
    viewModel: PhoneCountryViewModel,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        onDismissRequest = viewModel.dismiss,
        modifier = modifier
    ) {
        Surface {  }
    }
}