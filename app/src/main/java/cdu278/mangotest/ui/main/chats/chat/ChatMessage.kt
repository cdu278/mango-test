package cdu278.mangotest.ui.main.chats.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cdu278.mangotest.ui.halfMargin
import cdu278.mangotest.ui.main.chats.ChatMessageSenderUi.Me
import cdu278.mangotest.ui.main.chats.ChatMessageSenderUi.Peer
import cdu278.mangotest.ui.main.chats.ChatMessageTime

@Composable
fun ChatMessage(
    message: ChatMessageUi,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = when (message.sender) {
            Me -> Alignment.CenterEnd
            Peer -> Alignment.CenterStart
        },
        modifier = modifier
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = when (message.sender) {
                    Me -> MaterialTheme.colorScheme.primary
                    Peer -> MaterialTheme.colorScheme.secondary
                },
            ),
        ) {
            Column(
                modifier = Modifier
                    .padding(halfMargin)
                    .widthIn(min = 100.dp, max = 250.dp)
            ) {
                Text(message.text)
                ChatMessageTime(
                    message.time,
                    modifier = Modifier
                        .align(Alignment.End)
                )
            }
        }
    }
}