package cdu278.mangotest.zodiac

enum class ZodiacSign(
    val interval: ZodiacSignInterval,
) {

    Aries(ZodiacSignInterval(startMonth = 3, startDay = 21, endMonth = 4, endDay = 20)),
    Taurus(ZodiacSignInterval(startMonth = 4, startDay = 21, endMonth = 5, endDay = 21)),
    Gemini(ZodiacSignInterval(startMonth = 5, startDay = 22, endMonth = 6, endDay = 21)),
    Cancer(ZodiacSignInterval(startMonth = 6, startDay = 22, endMonth = 7, endDay = 23)),
    Leo(ZodiacSignInterval(startMonth = 7, startDay = 24, endMonth = 8, endDay = 23)),
    Virgo(ZodiacSignInterval(startMonth = 8, startDay = 25, endMonth = 9, endDay = 23)),
    Libra(ZodiacSignInterval(startMonth = 9, startDay = 24, endMonth = 10, endDay = 23)),
    Scorpio(ZodiacSignInterval(startMonth = 10, startDay = 24, endMonth = 11, endDay = 22)),
    Sagittarius(ZodiacSignInterval(startMonth = 11, startDay = 23, endMonth = 12, endDay = 21)),
    Capricorn(ZodiacSignInterval(startMonth = 12, startDay = 22, endMonth = 1, endDay = 20)),
    Aquarius(ZodiacSignInterval(startMonth = 1, startDay = 21, endMonth = 2, endDay = 19)),
    Pisces(ZodiacSignInterval(startMonth = 2, startDay = 20, endMonth = 3, endDay = 20)),
}