package cdu278.mangotest.ui.main.chats.chat

data class ChatUi(
    val peerName: String,
    val messages: List<ChatMessageUi>,
)