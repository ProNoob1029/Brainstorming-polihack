package com.dragos.brainstorming

import android.app.Application
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager

class MainApplication : Application() {
    companion object {
        lateinit var usageStatsManager: UsageStatsManager
        lateinit var packageManager: PackageManager
    }

    override fun onCreate() {
        super.onCreate()
        usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        MainApplication.packageManager = packageManager
    }
}