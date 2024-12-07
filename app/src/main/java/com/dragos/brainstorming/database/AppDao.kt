package com.dragos.brainstorming.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AppDao {
    @Query("SELECT * FROM goodapp")
    fun getGoodApps(): List<GoodApp>

    @Query("SELECT * FROM badapp")
    fun getBadApps(): List<BadApp>

    @Query("Select * FROM setlimit")
    fun getSetLimit(): List<SetLimit>

    @Insert
    fun insertGoodApp(app: GoodApp)

    @Delete
    fun deleteGoodApp(app: GoodApp)

    @Insert
    fun insertBadApp(app: BadApp)

    @Delete
    fun deleteBadApp(app: BadApp)

    @Insert
    fun insertSetLimit(app: SetLimit)

    @Delete
    fun deleteSetLimit(app: SetLimit)


}
