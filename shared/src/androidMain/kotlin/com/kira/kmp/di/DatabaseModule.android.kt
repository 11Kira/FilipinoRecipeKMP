package com.kira.kmp.di

import com.kira.kmp.data.local.database.AppDatabase
import com.kira.kmp.data.local.database.createRoomDatabase
import com.kira.kmp.data.local.database.getDatabaseBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual fun platformDatabaseModule() = module {
    single<AppDatabase> {
        val builder = getDatabaseBuilder(androidContext())
        createRoomDatabase(builder)
    }
}
