package com.kira.kmp.di

import com.kira.kmp.data.local.EncryptedStorage
import com.kira.kmp.data.local.IosEncryptedStorage
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single<EncryptedStorage> { IosEncryptedStorage() }
}
