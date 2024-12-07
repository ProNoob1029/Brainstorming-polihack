package com.dragos.brainstorming.monitor

import android.accessibilityservice.AccessibilityService
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.dragos.brainstorming.alert.AlertActivity


class MonitorService: AccessibilityService() {
    private lateinit var usageStatsManager: UsageStatsManager
    private val appPackageName = "com.instagram.android"

    private var firstEvent = true

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.packageName == appPackageName) {
            if (!firstEvent) return
            firstEvent = false

            val stat = getDailyStats(usageStatsManager).first { it.packageName == appPackageName }

            Log.d("Current App", "insta usage: ${stat.totalTime / (1000 * 60)}")

            val intent = Intent(this, AlertActivity::class.java)
                .setFlags(FLAG_ACTIVITY_NEW_TASK)

            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(intent)
            }, 1000)
        } else {
            firstEvent = true
        }
    }

    override fun onServiceConnected() {
        Log.d("CurrentApp", "HIIII")
    }

    override fun onCreate() {
        super.onCreate()
        usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    }

    override fun onInterrupt() {}
}