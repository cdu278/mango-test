package cdu278.mangotest.ui.error.request

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import cdu278.mangotest.R
import cdu278.mangotest.ui.error.ErrorDialog
import cdu278.mangotest.ui.error.request.RequestErrorDialogUi.ConnectionError
import cdu278.mangotest.ui.error.request.RequestErrorDialogUi.UnknownError
import cdu278.mangotest.ui.error.request.RequestErrorDialogUi.ValidationError

@Composable
fun RequestErrorDialog(
    model: RequestErrorDialogUi?,
    onDismiss: () -> Unit,
) {
    ErrorDialog(
        text = when (val m = model ?: return) {
            is ValidationError -> m.message
            is ConnectionError -> stringResource(R.string.requestErrorDialog_connection_text)
            is UnknownError -> stringResource(R.string.requestErrorDialog_unknown_text)
        },
        onDismiss = onDismiss,
    )
}