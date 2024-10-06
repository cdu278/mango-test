package cdu278.mangotest.zodiac

import kotlinx.datetime.LocalDate

class ZodiacSignInterval(
    private val startMonth: Int,
    private val startDay: Int,
    private val endMonth: Int,
    private val endDay: Int,
) {

    operator fun contains(date: LocalDate): Boolean {
        val month = date.monthNumber
        val day = date.dayOfMonth
        return (month == startMonth && day >= startDay) ||
                (month == endMonth && day <= endDay)
    }
}