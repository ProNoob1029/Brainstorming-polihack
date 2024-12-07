package com.dragos.brainstorming.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BadApp(
    @PrimaryKey val packageName: String,
    val appName: String
)