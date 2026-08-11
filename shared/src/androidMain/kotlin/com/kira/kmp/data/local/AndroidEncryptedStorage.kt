package com.kira.kmp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "secure_tokens")

class AndroidEncryptedStorage(private val context: Context) : EncryptedStorage {

    private val aead: Aead by lazy {
        AeadConfig.register()
        AndroidKeysetManager.Builder()
            .withSharedPref(context, "tink_keyset", "tink_key")
            .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
            .withMasterKeyUri("android-keystore://tink_master_key")
            .build()
            .keysetHandle
            .getPrimitive(Aead::class.java)
    }

    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun putString(key: String, value: String) {
        val encrypted = aead.encrypt(value.encodeToByteArray(), null)
        val base64 = Base64.encode(encrypted)
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = base64
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun getString(key: String): String? {
        val base64 =
            context.dataStore.data.map { it[stringPreferencesKey(key)] }.first() ?: return null
        return try {
            val encrypted = Base64.decode(base64)
            val decrypted = aead.decrypt(encrypted, null)
            decrypted.decodeToString()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun remove(key: String) {
        context.dataStore.edit { it.remove(stringPreferencesKey(key)) }
    }

    override suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}