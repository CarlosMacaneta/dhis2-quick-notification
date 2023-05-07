package org.saudigitus.quicknotification.ui.util

import android.os.Build
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


object DateTimeHelper {

    fun getTime(date: String): String {
        val dateTime = LocalDateTime.parse(date)

        return "${dateTime.hour}:${dateTime.minute}"
    }

    fun getDate(date: String): String {
        val dtfDate = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
        return dtfDate.format(Instant.parse(date))
    }

    fun ddMMDate(
        date: String
    ): String {
        val ldtDate = LocalDateTime.parse(date)
        return String.format("%02d", ldtDate.dayOfMonth).trim() +
            "/${String.format("%02d", ldtDate.monthValue)}".trim()
    }
}