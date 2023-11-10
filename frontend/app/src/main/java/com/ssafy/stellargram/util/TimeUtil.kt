package com.ssafy.stellargram.util

import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

object TimeUtil {
    private val yearMonthDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("y.M.d")
    private val updateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    private fun calZonedTime(unixTimestamp:Long): ZonedDateTime?{
        val zoneId = ZoneId.systemDefault()
        val instant: Instant = Instant.ofEpochMilli(unixTimestamp)
        return ZonedDateTime.ofInstant(instant, zoneId)
    }

    fun getYearMonth(unixTimestamp: Long, formatter: DateTimeFormatter = yearMonthDateFormatter): String{
        return formatter.format(calZonedTime(unixTimestamp))
    }

    fun getHourMinute(unixTimestamp: Long,formatter: DateTimeFormatter = updateTimeFormatter): String{
        return formatter.format(calZonedTime(unixTimestamp))
    }




}