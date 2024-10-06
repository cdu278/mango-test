package cdu278.mangotest.ui.main.profile.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import cdu278.mangotest.R
import cdu278.mangotest.ui.main.profile.dialog.ProfileDialogUi.Logout
import cdu278.mangotest.ui.main.profile.dialog.ProfileDialogUi.PickBirthday

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDialog(
    dialog: ProfileDialogUi?,
    onBirthdaySelected: (Long?) -> Unit,
    logOut: () -> Unit,
    onDismiss: () -> Unit,
) {
    when (dialog ?: return) {
        PickBirthday -> {
            val state = rememberDatePickerState()
            DatePickerDialog(
                onDismissRequest = onDismiss,
                confirmButton = {
                    TextButton(
                        onClick = {
                            onBirthdaySelected(state.selectedDateMillis)
                            onDismiss()
                        }
                    ) {
                        Text(stringResource(android.R.string.ok))
                    }
                },
            ) {
                DatePicker(state)
            }
        }
        Logout ->
            AlertDialog(
                onDismissRequest = onDismiss,
                confirmButton = {
                    TextButton(onClick = logOut) {
                        Text(stringResource(R.string.yes))
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.no))
                    }
                },
                title = {
                    Text(stringResource(R.string.logoutDialog_title))
                },
                text = {
                    Text(stringResource(R.string.logoutDialog_text))
                },
            )
    }
}