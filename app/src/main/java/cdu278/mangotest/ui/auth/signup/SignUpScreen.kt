package cdu278.mangotest.ui.auth.signup

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import cdu278.mangotest.R
import cdu278.mangotest.ui.auth.AuthScreen
import cdu278.mangotest.ui.auth.signup.SignUpEvent.SignedUp
import cdu278.mangotest.ui.auth.signup.SignUpUi.Error.EmptyName
import cdu278.mangotest.ui.auth.signup.SignUpUi.Error.EmptyUsername
import cdu278.mangotest.ui.auth.signup.SignUpUi.Error.InvalidUsername
import cdu278.mangotest.ui.defaultMargin
import cdu278.mangotest.ui.error.request.RequestErrorDialog

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    goToMain: () -> Unit,
    goBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(null) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                SignedUp -> goToMain()
            }
        }
    }
    AuthScreen(
        title = R.string.signUp_title,
        goBack = goBack,
        modifier = modifier
    ) {
        val model by viewModel.modelFlow.collectAsState()

        OutlinedTextField(
            value = model.name,
            onValueChange = viewModel::inputName,
            label = { Text(stringResource(R.string.signUp_name)) },
            placeholder = { Text(stringResource(R.string.signUp_name_placeholder)) },
            singleLine = true,
            isError = model.error == EmptyName,
            modifier = Modifier
                .fillMaxWidth()
        )

        OutlinedTextField(
            value = model.username,
            onValueChange = viewModel::inputUsername,
            label = { Text(stringResource(R.string.signUp_username)) },
            placeholder = { Text(stringResource(R.string.signUp_username_placeholder)) },
            singleLine = true,
            isError = model.error.let { it == EmptyUsername || it == InvalidUsername },
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(Modifier.height(defaultMargin))

        Text(
            text = when (model.error) {
                EmptyName -> stringResource(R.string.signUp_error_emptyName)
                EmptyUsername -> stringResource(R.string.signUp_error_emptyUsername)
                InvalidUsername -> stringResource(R.string.signUp_error_invalidUsername)
                null -> ""
            },
            color = MaterialTheme.colorScheme.error,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Button(
            onClick = viewModel::signUp,
            enabled = model.error == null && !model.loading,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(
                    id = if (model.loading) {
                        R.string.loading
                    } else {
                        R.string.signUp_signUp
                    }
                )
            )
        }

        RequestErrorDialog(
            model = model.dialog,
            onDismiss = viewModel::dismissDialog,
        )
    }
}