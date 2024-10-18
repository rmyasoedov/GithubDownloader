package com.instrument.githubdownloader

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.instrument.githubdownloader.dagger.AppComponent
import com.instrument.githubdownloader.dagger.DaggerAppComponent
import com.instrument.githubdownloader.dagger.module.AppModule

class MyApp : Application() {

    companion object{
        private lateinit var appComponent: AppComponent
        fun getComponent(): AppComponent = appComponent
        lateinit var globalContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        globalContext = this
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}