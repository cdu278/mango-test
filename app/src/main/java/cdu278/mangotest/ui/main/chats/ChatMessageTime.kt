package cdu278.mangotest.ui.main.chats

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import cdu278.mangotest.R
import cdu278.mangotest.ui.format
import cdu278.mangotest.ui.main.chats.ChatMessageTimeUi.Day.NotToday
import cdu278.mangotest.ui.main.chats.ChatMessageTimeUi.Day.Today
import kotlinx.datetime.LocalTime

@Composable
fun ChatMessageTime(
    time: ChatMessageTimeUi,
    modifier: Modifier = Modifier
) {
    Text(
        text = when (val day = time.day) {
            is Today -> time.time.format()
            is NotToday ->
                stringResource(
                    R.string.chats_time_notTodayFmt,
                    time.time.format(),
                    day.date.format(),
                )
        },
        fontSize = 12.sp,
        modifier = modifier
    )
}

@Composable
private fun LocalTime.format(): String {
    return stringResource(R.string.timeFmt, hour, minute)
}