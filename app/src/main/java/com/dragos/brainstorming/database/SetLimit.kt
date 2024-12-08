package com.dragos.brainstorming.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SetLimit(
    @PrimaryKey val packageName: String,
    val appName: String,
    val minuteLimit: Int
)