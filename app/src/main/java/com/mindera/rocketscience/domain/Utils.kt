package com.mindera.rocketscience.domain

import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.math.abs

fun dateFormatter(unix: Long): Pair<String, String> {
    val ldt = convertUnixToLocalDateTime(unix)
    val dateString = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(ldt)
    val timePattern = "HH:mm:ss"
    val dtf = DateTimeFormatter.ofPattern(timePattern)
    val timeString = dtf.format(ldt)
    return Pair(dateString, timeString)
}
fun convertUnixToLocalDateTime(unix: Long): LocalDateTime =
    LocalDateTime.ofInstant(Instant.ofEpochSecond(unix), ZoneId.systemDefault())
fun daysBetween(today: LocalDateTime, lunchDate: LocalDateTime): Long {
    val dur = Duration.between(today, lunchDate)
    return dur.toDays()
}

fun Boolean.toInt() = if (this) 1 else 0