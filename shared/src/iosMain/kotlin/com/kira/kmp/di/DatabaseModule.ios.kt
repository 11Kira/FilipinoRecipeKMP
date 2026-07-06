package com.kira.kmp.di

import com.kira.kmp.data.local.AppDatabase
import com.kira.kmp.data.local.createRoomDatabase
import com.kira.kmp.data.local.getDatabaseBuilder
import org.koin.dsl.module

actual fun platformDatabaseModule() = module {
    single<AppDatabase> {
        val builder = getDatabaseBuilder()
        createRoomDatabase(builder)
    }
    single { get<AppDatabase>().recipeDao() }
}
