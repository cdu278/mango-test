package cdu278.mangotest.ui.auth

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cdu278.mangotest.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    goBack: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    if (goBack != null) {
                        IconButton(onClick = goBack) {
                            Icon(
                                painterResource(R.drawable.ic_back),
                                contentDescription = null
                            )
                        }
                    }
                },
            )
        },
        modifier = modifier
    ) { paddings ->
        Box(
            modifier = Modifier
                .padding(paddings)
                .consumeWindowInsets(paddings)
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 40.dp)
            )

            Column(
                content = content,
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(270.dp)
            )
        }
    }
}