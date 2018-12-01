package com.danielecampogiani.demo.inject

import android.app.Application
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@Module
class AppModule(private val application: Application) {

    @Provides
    fun provideContext() = application

    @Provides
    fun provideBackgroundCoroutineContext(): CoroutineContext = Dispatchers.IO
}