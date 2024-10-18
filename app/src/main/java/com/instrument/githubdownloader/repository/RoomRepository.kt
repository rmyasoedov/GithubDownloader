package com.instrument.githubdownloader.repository

import androidx.lifecycle.LiveData
import com.instrument.githubdownloader.model.Load
import com.instrument.githubdownloader.room.Dao
import javax.inject.Inject

class RoomRepository @Inject constructor(private val dataBase: Dao) {

    suspend fun addLoad(load: Load): Long{
        return dataBase.addLoad(load)
    }

    fun getDownloads(): LiveData<List<Load>>{
        return dataBase.getDownloads()
    }
}