package com.instrument.githubdownloader.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.instrument.githubdownloader.model.Load

@Dao
interface Dao {

    @Insert
    suspend fun addLoad(load: Load): Long

    @Query("SELECT *FROM load ORDER BY timestamp DESC")
    fun getDownloads(): LiveData<List<Load>>
}