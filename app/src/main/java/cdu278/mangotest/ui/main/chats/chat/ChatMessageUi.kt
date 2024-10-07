package cdu278.mangotest.ui.main.chats.chat

import cdu278.mangotest.ui.main.chats.ChatMessageSenderUi
import cdu278.mangotest.ui.main.chats.ChatMessageTimeUi

data class ChatMessageUi(
    val id: Int,
    val text: String,
    val sender: ChatMessageSenderUi,
    val time: ChatMessageTimeUi,
)