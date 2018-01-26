package com.danielecampogiani.demo.inject

import android.app.Application
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers

@Module
class AppModule(private val application: Application) {

    @Provides
    fun provideContext() = application

    @Provides
    fun provideBackgroundScheduler() = Schedulers.io()
}