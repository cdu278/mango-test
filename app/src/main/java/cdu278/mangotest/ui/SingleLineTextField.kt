package cdu278.mangotest.ui

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import cdu278.mangotest.R

@Composable
fun SingleLineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    supportingText: (@Composable () -> Unit)? = null,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    val focusRequester = remember { FocusRequester() }
    OutlinedTextField(
        value,
        onValueChange,
        singleLine = true,
        label = label,
        placeholder = placeholder,
        supportingText = supportingText,
        readOnly = readOnly,
        enabled = enabled,
        isError = isError,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        trailingIcon = trailingIcon ?: if (value.isNotEmpty() && !readOnly && enabled) {
            {
                IconButton(
                    onClick = {
                        onValueChange("")
                        focusRequester.requestFocus()
                    }
                ) {
                    Icon(painterResource(R.drawable.ic_clear), contentDescription = null)
                }
            }
        } else {
            null
        },
        modifier = modifier
            .focusRequester(focusRequester),
    )
}