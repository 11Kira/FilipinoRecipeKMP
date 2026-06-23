package com.kira.kmp.di

import com.russhwolf.multiplatform.settings.Settings
import com.russhwolf.multiplatform.settings.SharedPreferencesSettings
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual fun platformSettingsModule() = module {
    single<Settings> {
        val sharedPrefs = androidContext().getSharedPreferences("FilipinoRecipeKMP_prefs", 0)
        SharedPreferencesSettings(sharedPrefs)
    }
}
