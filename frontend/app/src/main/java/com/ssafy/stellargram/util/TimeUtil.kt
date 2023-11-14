package com.ssafy.stellargram.util

import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

object TimeUtil {
    private val yearMonthDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yy.MM.dd")
    private val updateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    private fun calZonedTime(unixTimestamp: Long): ZonedDateTime? {
        val zoneId = ZoneId.systemDefault()
        val instant: Instant = Instant.ofEpochMilli(unixTimestamp)
        return ZonedDateTime.ofInstant(instant, zoneId)
    }

    fun getYearMonth(unixTimestamp: Long): String {
        return if (unixTimestamp.toString().length > 10) yearMonthDateFormatter.format(
            calZonedTime(
                unixTimestamp
            )
        )
        else yearMonthDateFormatter.format(calZonedTime(unixTimestamp * 1000))
    }

    fun getHourMinute(unixTimestamp: Long): String {
        return if (unixTimestamp.toString().length > 10) updateTimeFormatter.format(
            calZonedTime(
                unixTimestamp
            )
        )
        else updateTimeFormatter.format(calZonedTime(unixTimestamp * 1000))
    }


}