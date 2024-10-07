package cdu278.mangotest.ui.main.chats.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cdu278.mangotest.ui.main.chats.ChatMessageSenderUi.Me
import cdu278.mangotest.ui.main.chats.ChatMessageSenderUi.Peer
import cdu278.mangotest.ui.main.chats.ChatMessageTimeUi
import cdu278.mangotest.ui.main.chats.ChatMessageTimeUi.Day.NotToday
import cdu278.mangotest.ui.main.chats.ChatMessageTimeUi.Day.Today
import cdu278.mangotest.ui.uiSharingStarted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {

    val modelFlow: StateFlow<ChatUi?> =
        flow {
            emit(
                ChatUi(
                    peerName = "Huan Zai",
                    messages = listOf(
                        ChatMessageUi(
                            id = 1,
                            text = "Hi",
                            sender = Peer,
                            time = ChatMessageTimeUi(
                                NotToday(LocalDate(2024, 10, 5)),
                                LocalTime(10, 1)
                            ),
                        ),
                        ChatMessageUi(
                            id = 2,
                            text = "Hello",
                            sender = Me,
                            time = ChatMessageTimeUi(
                                NotToday(LocalDate(2024, 10, 5)),
                                LocalTime(10, 2)
                            ),
                        ),
                        ChatMessageUi(
                            id = 3,
                            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                            sender = Me,
                            time = ChatMessageTimeUi(
                                NotToday(LocalDate(2024, 10, 5)),
                                LocalTime(10, 5)
                            ),
                        ),
                        ChatMessageUi(
                            id = 4,
                            text = "Nullam semper blandit molestie. Etiam tempor tempor fringilla. Vivamus eget massa iaculis, gravida nisl id, hendrerit sapien. Pellentesque pulvinar urna sit amet cursus elementum. Donec tincidunt urna nisl, eu dapibus ex aliquam pretium.",
                            sender = Peer,
                            time = ChatMessageTimeUi(Today, LocalTime(8, 13)),
                        ),
                        ChatMessageUi(
                            id = 5,
                            text = "Donec elementum risus enim",
                            sender = Peer,
                            time = ChatMessageTimeUi(Today, LocalTime(8, 13)),
                        ),
                        ChatMessageUi(
                            id = 6,
                            text = "Donec eu dapibus mauris. Nulla vitae ligula quis lacus molestie venenatis id quis magna. Ut ut nunc sed nisl gravida tempor vel ut massa. Integer sed facilisis ipsum. Nunc ut tempor dolor, elementum imperdiet arcu.",
                            sender = Me,
                            time = ChatMessageTimeUi(Today, LocalTime(9, 30)),
                        ),
                        ChatMessageUi(
                            id = 7,
                            text = "Donec lobortis",
                            sender = Me,
                            time = ChatMessageTimeUi(Today, LocalTime(9, 30)),
                        ),
                        ChatMessageUi(
                            id = 8,
                            text = "euismod purus auctor feugiat",
                            sender = Me,
                            time = ChatMessageTimeUi(Today, LocalTime(9, 30)),
                        ),
                        ChatMessageUi(
                            id = 9,
                            text = "Phasellus ullamcorper lorem dolor, porttitor sollicitudin est eleifend sed.",
                            sender = Peer,
                            time = ChatMessageTimeUi(Today, LocalTime(13, 44)),
                        ),
                        ChatMessageUi(
                            id = 10,
                            text = "OK",
                            sender = Me,
                            time = ChatMessageTimeUi(Today, LocalTime(18, 59)),
                        ),
                    ),
                )
            )
        }.stateIn(viewModelScope, uiSharingStarted, initialValue = null)
}