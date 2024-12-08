package com.dragos.brainstorming

data class AppInfo(
    val appTime: Long,
    val appName: String,
    val packageName: String,
    val timeLimit: Int = 5
)