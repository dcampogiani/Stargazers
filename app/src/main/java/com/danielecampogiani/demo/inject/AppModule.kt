package com.danielecampogiani.demo.inject

import android.app.Application
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.experimental.CommonPool
import kotlin.coroutines.experimental.CoroutineContext

@Module
class AppModule(private val application: Application) {

    @Provides
    fun provideContext() = application

    @Provides
    fun provideBackgroundCoroutineContext(): CoroutineContext = CommonPool
}