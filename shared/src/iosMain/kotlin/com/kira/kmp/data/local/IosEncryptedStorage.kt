package com.kira.kmp.data.local

import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.KeychainSettings
import com.russhwolf.settings.set

@OptIn(ExperimentalSettingsImplementation::class)
class IosEncryptedStorage : EncryptedStorage {
    private val settings = KeychainSettings("recipe_secure")

    override suspend fun putString(key: String, value: String) {
        settings[key] = value
    }

    override suspend fun getString(key: String): String? {
        return settings.getStringOrNull(key)
    }

    override suspend fun remove(key: String) {
        settings.remove(key)
    }

    override suspend fun clear() {
        settings.clear()
    }
}