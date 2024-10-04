package cdu278.mangotest.ui.auth.signin

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import cdu278.mangotest.R
import cdu278.mangotest.ui.auth.AuthScreen
import cdu278.mangotest.ui.auth.phone.PhoneField
import cdu278.mangotest.ui.auth.signin.SignInEvent.Authorized
import cdu278.mangotest.ui.auth.signin.SignInEvent.SignUpNeeded
import cdu278.mangotest.ui.auth.signin.SignInUi.State.CodeInput
import cdu278.mangotest.ui.auth.signin.SignInUi.State.PhoneInput
import cdu278.mangotest.ui.defaultMargin

@Composable
fun SignInScreen(
    viewModel: SignInViewModel,
    goToMain: () -> Unit,
    goToSignUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(null) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                Authorized -> goToMain()
                SignUpNeeded -> goToSignUp()
            }
        }
    }
    AuthScreen(
        title = R.string.signIn_title,
        modifier = modifier
    ) {
        val model by viewModel.modelFlow.collectAsState()

        PhoneField(
            viewModel.phoneViewModel,
            imeAction = when (model.state) {
                is PhoneInput -> ImeAction.Done
                is CodeInput -> ImeAction.Next
            },
            actions = when (model.state) {
                is PhoneInput -> KeyboardActions(onDone = { viewModel.sendCode() })
                is CodeInput -> KeyboardActions.Default
            },
            enabled = !model.loading,
        )

        when (val s = model.state) {
            is PhoneInput -> {}
            is CodeInput ->
                SmsCodeField(
                    value = s.code,
                    onValueChange = viewModel::inputCode,
                    onDone = viewModel::checkCode,
                    isError = !model.isValid,
                    enabled = !model.loading,
                    modifier = Modifier
                        .fillMaxWidth()
                )
        }

        Spacer(Modifier.height(defaultMargin))
        Button(
            onClick = when (model.state) {
                is PhoneInput -> viewModel::sendCode
                is CodeInput -> viewModel::checkCode
            },
            enabled = model.isValid && !model.loading,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(
                    id = if (model.loading) {
                        R.string.loading
                    } else {
                        when (model.state) {
                            is PhoneInput -> R.string.signIn_sendCode
                            is CodeInput -> R.string.signIn_checkCode
                        }
                    }
                )
            )
        }

        SignInDialog(
            model = model.dialog,
            onDismiss = viewModel::dismissDialog,
        )
    }
}