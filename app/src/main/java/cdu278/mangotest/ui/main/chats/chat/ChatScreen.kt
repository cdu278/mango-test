package cdu278.mangotest.ui.main.chats.chat

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    goBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text("Chat")
}