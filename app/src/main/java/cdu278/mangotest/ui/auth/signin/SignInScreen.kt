package cdu278.mangotest.ui.auth.signin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import cdu278.mangotest.R
import cdu278.mangotest.ui.auth.authTitleTopPadding
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
    goToSignUp: (phone: String) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(null) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is Authorized -> goToMain()
                is SignUpNeeded -> goToSignUp(event.phone)
            }
        }
    }
    Scaffold(
        modifier = modifier
    ) { paddings ->
        Box(
            modifier = Modifier
                .padding(paddings)
                .consumeWindowInsets(paddings)
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.signIn_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = authTitleTopPadding)
            )

            val model by viewModel.modelFlow.collectAsState()

            Column(
                modifier = Modifier
                    .width(270.dp)
                    .align(Alignment.Center)
            ) {
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
            }

            Spacer(Modifier)

            SignInDialog(
                model = model.dialog,
                onDismiss = viewModel::dismissDialog,
            )
        }
    }
}