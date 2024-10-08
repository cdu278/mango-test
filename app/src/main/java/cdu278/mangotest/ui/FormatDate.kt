package cdu278.mangotest.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.char

private val format =
    LocalDate.Format { dayOfMonth(); char('.'); monthNumber(); char('.'); year() }

@Composable
fun LocalDate.format(): String {
    return remember(year, month, dayOfMonth) { format(format) }
}