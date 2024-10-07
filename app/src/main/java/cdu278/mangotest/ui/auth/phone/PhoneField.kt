package cdu278.mangotest.ui.auth.phone

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cdu278.mangotest.R
import cdu278.mangotest.ui.auth.phone.country.PhoneCountryPickerDialog

@Composable
fun PhoneField(
    viewModel: PhoneViewModel,
    imeAction: ImeAction,
    actions: KeyboardActions,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    val phone by viewModel.phoneFlow.collectAsState()
    val model by viewModel.modelFlow.collectAsState()

    model.countryPicker?.let {
        PhoneCountryPickerDialog(
            it.viewModel,
            modifier = Modifier
                .height(500.dp)
        )
    }

    OutlinedTextField(
        value = phone,
        onValueChange = viewModel::input,
        isError = !model.isValid,
        singleLine = true,
        label = { Text(stringResource(R.string.signIn_phoneNumber)) },
        leadingIcon = {
            IconButton(onClick = viewModel::chooseCountry) {
                model.flagResId
                    ?.let { Image(painterResource(it), contentDescription = null) }
                    ?: Icon(painterResource(R.drawable.ic_globe), contentDescription = null)
            }
        },
        trailingIcon = {
            if (phone.isNotEmpty()) {
                IconButton(onClick = { viewModel.input("") }) {
                    Icon(painterResource(R.drawable.ic_clear), contentDescription = null)
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            imeAction = imeAction,
        ),
        keyboardActions = actions,
        enabled = enabled,
        visualTransformation = model.formatter
            ?.let(::PhoneVisualTransformation)
            ?: VisualTransformation.None,
        modifier = modifier,
    )
}