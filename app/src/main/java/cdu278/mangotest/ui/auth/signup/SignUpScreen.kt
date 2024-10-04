package cdu278.mangotest.ui.auth.signup

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
    ) { paddings ->
        Text(
            text = "Sign up",
            modifier = Modifier
                .padding(paddings)
                .consumeWindowInsets(paddings)
        )
    }
}