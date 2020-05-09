package com.anibalbastias.androidranduser

import android.app.Application
import android.os.StrictMode
import com.anibalbastias.androidranduser.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

open class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                appModule
            )
        }

        setupRestrictionsForShare()
    }

    private fun setupRestrictionsForShare() {
        val builder: StrictMode.VmPolicy.Builder =
            StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
    }
}