package com.dragos.brainstorming.monitor

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Returns the stats for the [date] (defaults to today)
 */
fun getDailyStats(usageManager: UsageStatsManager, date: LocalDate = LocalDate.now()): List<Stat> {
    // The timezones we'll need
    val utc = ZoneId.of("UTC")
    val defaultZone = ZoneId.systemDefault()

    // Set the starting and ending times to be midnight in UTC time
    val startDate = date.atStartOfDay(defaultZone).withZoneSameInstant(utc)
    val start = startDate.toInstant().toEpochMilli()
    val end = startDate.plusDays(1).toInstant().toEpochMilli()

    // This will keep a map of all of the events per package name
    val sortedEvents = mutableMapOf<String, MutableList<UsageEvents.Event>>()

    // Query the list of events that has happened within that time frame
    val systemEvents = usageManager.queryEvents(start, end)
    while (systemEvents.hasNextEvent()) {
        val event = UsageEvents.Event()
        systemEvents.getNextEvent(event)

        // Get the list of events for the package name, create one if it doesn't exist
        val packageEvents = sortedEvents[event.packageName] ?: mutableListOf()
        packageEvents.add(event)
        sortedEvents[event.packageName] = packageEvents
    }

    // This will keep a list of our final stats
    val stats = mutableListOf<Stat>()

    // Go through the events by package name
    sortedEvents.forEach { (packageName, events) ->
        // Keep track of the current start and end times
        var startTime = 0L
        var endTime = 0L
        // Keep track of the total usage time for this app
        var totalTime = 0L
        // Keep track of the start times for this app
        val startTimes = mutableListOf<ZonedDateTime>()
        events.forEach {
            if (it.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                // App was moved to the foreground: set the start time
                startTime = it.timeStamp
                // Add the start time within this timezone to the list
                startTimes.add(
                    Instant.ofEpochMilli(startTime).atZone(utc)
                        .withZoneSameInstant(defaultZone))
            } else if (it.eventType == UsageEvents.Event.ACTIVITY_PAUSED) {
                // App was moved to background: set the end time
                endTime = it.timeStamp
            }

            // If there's an end time with no start time, this might mean that
            //  The app was started on the previous day, so take midnight
            //  As the start time
            if (startTime == 0L && endTime != 0L) {
                startTime = start
            }

            // If both start and end are defined, we have a session
            if (startTime != 0L && endTime != 0L) {
                // Add the session time to the total time
                totalTime += endTime - startTime
                // Reset the start/end times to 0
                startTime = 0L
                endTime = 0L
            }
        }

        if (startTime != 0L) {
            totalTime += System.currentTimeMillis() - startTime
        }
        stats.add(Stat(packageName, totalTime/*, startTimes*/))
    }
    return stats
}

// Helper class to keep track of all of the stats
class Stat(val packageName: String, val totalTime: Long/*, val startTimes: List<ZonedDateTime>*/)