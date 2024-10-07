package cdu278.mangotest.ui.main.chats.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import cdu278.mangotest.R
import cdu278.mangotest.ui.halfMargin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    goBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val model by viewModel.modelFlow.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = model?.peerName ?: "") },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(painterResource(R.drawable.ic_back), contentDescription = null)
                    }
                },
            )
        },
        modifier = modifier
    ) { paddings ->
        Column(
            modifier = Modifier
                .padding(paddings)
                .consumeWindowInsets(paddings)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                val messages = model?.messages ?: return@Box
                if (messages.isEmpty()) {
                    Text(
                        stringResource(R.string.chat_noMessages),
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                } else {
                    val listState =
                        rememberLazyListState(
                            initialFirstVisibleItemIndex = messages.size - 1
                        )
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(halfMargin),
                        state = listState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(halfMargin)
                            .align(Alignment.BottomCenter)
                    ) {
                        items(messages, key = ChatMessageUi::id) {
                            ChatMessage(
                                message = it,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
            }

            var message by remember { mutableStateOf("") }
            TextField(
                value = message,
                onValueChange = { message = it },
                trailingIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            painterResource(R.drawable.ic_send),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
                placeholder = { Text(stringResource(R.string.chat_messageField_placeholder)) },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                shape = RectangleShape,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                ),
                modifier = Modifier
                    .heightIn(max = 100.dp)
                    .fillMaxWidth()
            )
        }
    }
}