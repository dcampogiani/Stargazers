package com.danielecampogiani.demo

import android.app.Application
import android.content.Context
import com.danielecampogiani.demo.inject.AppComponent
import com.danielecampogiani.demo.inject.AppModule
import com.danielecampogiani.demo.inject.DaggerAppComponent

class DemoApplication : Application() {

    companion object {
        fun getAppComponent(context: Context): AppComponent {
            val app = context.applicationContext as DemoApplication
            return app.appComponent
        }
    }

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }
}