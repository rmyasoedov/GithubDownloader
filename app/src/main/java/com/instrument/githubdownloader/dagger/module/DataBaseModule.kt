package com.instrument.githubdownloader.dagger.module

import android.content.Context
import com.instrument.githubdownloader.room.Dao
import com.instrument.githubdownloader.room.RoomDb
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataBaseModule {

    @Provides
    @Singleton
    fun provideRoomDb(context: Context): RoomDb {
        return RoomDb.getDb(context.applicationContext)
    }

    @Provides
    @Singleton
    fun provideDao(context: Context): Dao {
        return RoomDb.getDb(context.applicationContext).getDao()
    }
}