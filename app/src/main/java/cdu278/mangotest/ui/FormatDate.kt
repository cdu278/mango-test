package cdu278.mangotest.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format

@Composable
fun LocalDate.format(): String {
    return remember(year, month, dayOfMonth) {
        format(LocalDate.Formats.ISO)
    }
}