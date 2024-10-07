package cdu278.mangotest.ui.main.chats

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class ChatMessageTimeUi(
    val day: Day,
    val time: LocalTime,
) {

    sealed interface Day {

        data object Today : Day

        data class NotToday(val date: LocalDate) : Day
    }
}