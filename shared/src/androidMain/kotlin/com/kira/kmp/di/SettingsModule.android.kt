package com.kira.kmp.di

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val PREF_NAME = "secure_prefs"

actual fun platformSettingsModule() = module {
    single<Settings> {
        val context = androidContext()
        val sharedPrefs = try {
            createEncryptedSharedPreferences(context)
        } catch (e: Exception) {
            Log.e(
                "SettingsModule",
                "Critical error initializing EncryptedSharedPreferences, resetting...",
                e
            )

            // 1. Delete the physical file
            context.deleteSharedPreferences(PREF_NAME)

            // 2. Try one more time
            try {
                createEncryptedSharedPreferences(context)
            } catch (e2: Exception) {
                // 3. Last resort: If it STILL fails, log it and provide a dummy SharedPreferences
                // so the app doesn't crash. The user will simply be "logged out".
                Log.e("SettingsModule", "Final attempt failed, returning unencrypted fallback", e2)
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            }
        }
        SharedPreferencesSettings(sharedPrefs)
    }
}

private fun createEncryptedSharedPreferences(context: Context): SharedPreferences {
    val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    return EncryptedSharedPreferences.create(
        context,
        PREF_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}
