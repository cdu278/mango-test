package cdu278.mangotest.ui.auth.signin

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SignInScreen(
    viewModel: SignInViewModel,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
    ) { paddings ->
        Text(
            text = "Sign In",
            modifier = Modifier
                .padding(paddings)
                .consumeWindowInsets(paddings)
        )
    }
}