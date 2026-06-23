package com.kira.kmp

import android.app.Application
import com.kira.kmp.di.initKoin
import org.koin.android.ext.koin.androidContext

class RecipeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@RecipeApplication)
        }
    }
}
