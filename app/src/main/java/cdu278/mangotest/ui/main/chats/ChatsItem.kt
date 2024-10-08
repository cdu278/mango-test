package cdu278.mangotest.ui.main.chats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cdu278.mangotest.R
import cdu278.mangotest.ui.defaultMargin
import cdu278.mangotest.ui.halfMargin
import cdu278.mangotest.ui.main.chats.ChatMessageSenderUi.Me
import cdu278.mangotest.ui.main.chats.ChatMessageSenderUi.Peer

@Composable
fun ChatsItem(
    item: ChatsItemUi,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(halfMargin)
    ) {
        ChatsAvatar(
            item.peerAvatarUrl,
            modifier = Modifier
                .size(64.dp)
        )
        Spacer(Modifier.width(defaultMargin))
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = item.peerName,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = when (item.sender) {
                    Me -> stringResource(R.string.chats_myMessageFmt, item.text)
                    Peer -> item.text
                },
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Spacer(Modifier.width(defaultMargin))
        ChatMessageTime(item.time)
    }
}