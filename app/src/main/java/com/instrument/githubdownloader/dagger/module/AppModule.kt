package com.instrument.githubdownloader.dagger.module

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.instrument.githubdownloader.ui.MainActivity
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {
    @Provides
    @Singleton
    fun provideContext(): Context = application
}

@Module
class MainActivityModule(private val mainActivity: MainActivity){

    @Provides
    fun provideAppCompatActivity(): AppCompatActivity = mainActivity
}