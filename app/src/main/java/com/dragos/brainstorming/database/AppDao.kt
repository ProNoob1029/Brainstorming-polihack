package com.dragos.brainstorming.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM goodapp")
    fun getGoodApps(): List<GoodApp>

    @Query("SELECT * FROM badapp")
    fun getBadApps(): List<BadApp>

    @Query("Select * FROM setlimit")
    fun getSetLimit(): List<SetLimit>

    @Query("Select * FROM setlimit")
    fun getSetLimitFlow(): Flow<List<SetLimit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGoodApp(app: GoodApp)

    @Delete
    fun deleteGoodApp(app: GoodApp)

    @Query("Select * FROM Tree WHERE name = :name")
    fun getTree(name: String): Tree?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTree(tree: Tree)

    @Delete
    fun deleteTree(tree: Tree)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBadApp(app: BadApp)

    @Delete
    fun deleteBadApp(app: BadApp)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSetLimit(app: SetLimit)

    @Delete
    fun deleteSetLimit(app: SetLimit)


}
