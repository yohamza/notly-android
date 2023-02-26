package io.notly.android.utils

import android.util.Log
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Hours
import org.joda.time.Minutes
import java.text.SimpleDateFormat
import java.util.*

class AppUtils {

    fun convertUtcTimeToLocalWithAgo(timeFromServer: String): String {
        var filteredTime = "N/A"
        try {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            formatter.timeZone = TimeZone.getTimeZone("UTC")

            val pasTime = formatter.parse(timeFromServer)
            val remoteDate = DateTime(pasTime)
            // Getting the current local date time
            val localDate = DateTime()
            // Calculating days between curernt time and remote time
            val days = Days.daysBetween(remoteDate, localDate).days
            // Calculating minutes between curernt time and remote time
            val minutes = Minutes.minutesBetween(remoteDate, localDate).minutes
            // Calculating hours between curernt time and remote time
            val hours = Hours.hoursBetween(remoteDate, localDate).hours

            filteredTime = when {
                days > 0 -> {
                    if (days == 1)
                        "$days day ago"
                    else
                        "$days days ago"

                }
                hours in 1..24 -> {
                    if (hours == 1)
                        "$hours hour ago"
                    else
                        "$hours hours ago"
                }
                minutes in 1..60 -> {
                    if (minutes == 1)
                        "$minutes min ago"
                    else
                        "$minutes mins ago"
                }
                minutes == 0 -> "just now"
                else -> "N/A"
            }

        } catch (e: Exception) {
            Log.d("", "getLastActiveTime: " + e.message)
        }

        return filteredTime

    }

}