package com.kira.kmp.di

import com.russhwolf.multiplatform.settings.NSUserDefaultsSettings
import com.russhwolf.multiplatform.settings.Settings
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

actual fun platformSettingsModule() = module {
    single<Settings> {
        val userDefaults = NSUserDefaults.standardUserDefaults
        NSUserDefaultsSettings(userDefaults)
    }
}
