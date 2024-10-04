package cdu278.mangotest.ui.auth.signin

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import cdu278.mangotest.R

@Composable
fun SmsCodeField(
    value: String,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit,
    isError: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value,
        onValueChange,
        label = { Text(stringResource(R.string.signIn_smsCode)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(
            onDone = { onDone() },
        ),
        supportingText = {
            Text(
                text = if (isError) {
                    stringResource(R.string.signIn_code_error)
                } else {
                    ""
                },
                color = MaterialTheme.colorScheme.error,
            )
        },
        isError = isError,
        enabled = enabled,
        modifier = modifier,
    )
}