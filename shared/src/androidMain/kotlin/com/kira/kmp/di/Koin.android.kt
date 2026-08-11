package com.kira.kmp.di

import com.kira.kmp.data.local.AndroidEncryptedStorage
import com.kira.kmp.data.local.EncryptedStorage
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single<EncryptedStorage> { AndroidEncryptedStorage(get()) }
}