package cdu278.mangotest.ui.auth.phone

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import cdu278.mangotest.R

@Composable
fun PhoneField(
    viewModel: PhoneViewModel,
    imeAction: ImeAction,
    actions: KeyboardActions,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    val value by viewModel.phoneNumberFlow.collectAsState()
    val isValid by viewModel.isValidFlow.collectAsState()
    OutlinedTextField(
        value,
        onValueChange = viewModel::input,
        isError = !isValid,
        singleLine = true,
        label = { Text(stringResource(R.string.signIn_phoneNumber)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            imeAction = imeAction,
        ),
        keyboardActions = actions,
        enabled = enabled,
        modifier = modifier,
    )
}