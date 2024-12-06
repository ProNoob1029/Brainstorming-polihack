package com.dragos.brainstorming

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent


class MonitorService: AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        Log.d("CurrentApp", "New event at ${event.packageName}")
    }

    override fun onServiceConnected() {
        Log.d("CurrentApp", "HIIII")
    }

    override fun onInterrupt() {

    }
}