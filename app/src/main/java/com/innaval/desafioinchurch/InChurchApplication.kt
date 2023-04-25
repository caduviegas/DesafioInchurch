package com.innaval.desafioinchurch

import android.app.Application
import com.innaval.desafioinchurch.di.Modules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class InChurchApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@InChurchApplication)
            modules(listOf(Modules.inChurchModule))
        }
    }
}