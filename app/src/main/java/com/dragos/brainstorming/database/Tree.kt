package com.dragos.brainstorming.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.filament.utils.Float3

@Entity
data class Tree(
    @PrimaryKey val name: String,
    val x: Float,
    val y: Float,
    val z: Float
)