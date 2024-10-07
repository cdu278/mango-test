package cdu278.mangotest.ui.main.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cdu278.mangotest.ui.main.chats.ChatMessageSenderUi.Me
import cdu278.mangotest.ui.main.chats.ChatMessageSenderUi.Peer
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
class ChatsViewModel @Inject constructor() : ViewModel() {

    val itemsFlow: StateFlow<List<ChatsItemUi>> =
        flow {
            emit(listOf(
                ChatsItemUi(
                    id = 1,
                    peerName = "John Smith",
                    peerAvatarUrl = "https://random.imagecdn.app/100/100",
                    text = "Some message",
                    sender = Me,
                    time = ChatMessageTimeUi(Today, LocalTime(12, 30)),
                ),
                ChatsItemUi(
                    id = 2,
                    peerName = "Jane Doe",
                    peerAvatarUrl = "https://random.imagecdn.app/101/101",
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                    sender = Peer,
                    time = ChatMessageTimeUi(
                        NotToday(LocalDate(2024, 7, 15)),
                        LocalTime(8, 21)
                    ),
                ),
                ChatsItemUi(
                    id = 3,
                    peerName = "Mike",
                    peerAvatarUrl = "https://random.imagecdn.app/102/102",
                    text = "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                    sender = Me,
                    time = ChatMessageTimeUi(
                        NotToday(LocalDate(2024, 9, 30)),
                        LocalTime(11, 0)
                    ),
                ),
            ))
        }.stateIn(viewModelScope, uiSharingStarted, initialValue = emptyList())
}