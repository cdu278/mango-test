package cdu278.mangotest.ui.main

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    goToAuth: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(null) {
        viewModel.authorizedFlow.collect { authorized ->
            if (!authorized) {
                goToAuth()
            }
        }
    }
    Scaffold(
        modifier = modifier
    ) {
        Text("Main")
    }
}