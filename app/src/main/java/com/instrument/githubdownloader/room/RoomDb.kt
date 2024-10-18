package com.instrument.githubdownloader.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.instrument.githubdownloader.model.Load

@Database(
    entities = [
        Load::class
    ],
    version = 1
)
abstract class RoomDb : RoomDatabase(){
    abstract fun getDao(): Dao

    companion object{
        fun getDb(context: Context): RoomDb{
            return Room.databaseBuilder(
                context.applicationContext,
                RoomDb::class.java,
                "room.db"
            ).build()
        }
    }
}