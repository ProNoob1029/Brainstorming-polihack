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
import com.dragos.brainstorming.AppInfo
import com.dragos.brainstorming.MainApplication
import com.dragos.brainstorming.alert.AlertActivity
import com.dragos.brainstorming.database.BadApp
import com.dragos.brainstorming.database.SetLimit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MonitorService: AccessibilityService() {
    private lateinit var usageStatsManager: UsageStatsManager

    private val dbJob = Job()
    private val scope = CoroutineScope(Dispatchers.IO + dbJob)
    private var limitList = listOf<SetLimit>()

    private var firstEvent = true

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        limitList.firstOrNull { it.packageName == event.packageName }?.let {  limitApp ->
            val timeLimit = getDailyStats(usageStatsManager).firstOrNull { it.packageName == limitApp.packageName }?.totalTime ?: 0
            if (timeLimit < limitApp.minuteLimit * 60 * 1000) {
                firstEvent = true
                return
            }
            if (!firstEvent) return
            firstEvent = false

            val intent = Intent(this, AlertActivity::class.java)
                .setFlags(FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

            /*Handler(Looper.getMainLooper()).postDelayed({

            }, 1000)*/
        }
        firstEvent = true
    }

    override fun onDestroy() {
        super.onDestroy()
        dbJob.cancel()
    }

    override fun onServiceConnected() {
        Log.d("CurrentApp", "HIIII")
    }

    override fun onCreate() {
        super.onCreate()
        usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        scope.launch {
            MainApplication.database.appDao().getSetLimitFlow().collect {
                limitList = it
            }
        }
    }

    override fun onInterrupt() {}
}