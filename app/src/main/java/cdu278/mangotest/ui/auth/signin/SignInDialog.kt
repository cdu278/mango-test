package cdu278.mangotest.ui.auth.signin

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import cdu278.mangotest.R
import cdu278.mangotest.ui.auth.signin.SignInDialogUi.InvalidCode
import cdu278.mangotest.ui.auth.signin.SignInDialogUi.RequestError
import cdu278.mangotest.ui.error.ErrorDialog
import cdu278.mangotest.ui.error.request.RequestErrorDialog

@Composable
fun SignInDialog(
    model: SignInDialogUi?,
    onDismiss: () -> Unit,
) {
    when (val m = model ?: return) {
        is InvalidCode ->
            ErrorDialog(
                text = stringResource(R.string.signIn_invalidCode),
                onDismiss,
            )
        is RequestError ->
            RequestErrorDialog(
                model = m.dialogUi,
                onDismiss,
            )
    }
}