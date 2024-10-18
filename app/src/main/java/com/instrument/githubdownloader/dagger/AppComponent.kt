package com.instrument.githubdownloader.dagger

import com.instrument.githubdownloader.dagger.module.NetworkModule
import com.instrument.githubdownloader.dagger.module.AppModule
import com.instrument.githubdownloader.dagger.module.DataBaseModule
import com.instrument.githubdownloader.dagger.module.MainActivityModule
import com.instrument.githubdownloader.ui.DownloadFragment
import com.instrument.githubdownloader.ui.MainActivity
import com.instrument.githubdownloader.ui.RepositoryFragment
import com.instrument.githubdownloader.ui.SearchFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent
import javax.inject.Scope
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    NetworkModule::class,
    DataBaseModule::class
])
interface AppComponent {
    fun mainActivitySubcomponent(): MainActivitySubcomponent.Factory
}

@ActivityScope
@Subcomponent(modules = [MainActivityModule::class])
interface MainActivitySubcomponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(mainActivityModule: MainActivityModule): MainActivitySubcomponent
    }

    fun inject(mainActivity: MainActivity)
    fun inject(searchFragment: SearchFragment)
    fun inject(repositoryFragment: RepositoryFragment)
    fun inject(downloadFragment: DownloadFragment)
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope