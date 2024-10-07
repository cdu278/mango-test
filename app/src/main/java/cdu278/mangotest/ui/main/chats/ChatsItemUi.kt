package cdu278.mangotest.ui.main.chats

data class ChatsItemUi(
    val id: Int,
    val peerName: String,
    val peerAvatarUrl: String,
    val text: String,
    val sender: ChatMessageSenderUi,
    val time: ChatMessageTimeUi,
)