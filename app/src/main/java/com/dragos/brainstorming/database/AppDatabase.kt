package com.dragos.brainstorming.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GoodApp::class, BadApp::class, SetLimit::class, Tree::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}
