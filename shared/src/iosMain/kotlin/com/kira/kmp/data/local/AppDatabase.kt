package com.kira.kmp.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = NSHomeDirectory() + "/recipe.db"
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile,
        factory = { AppDatabaseConstructor.initialize() }
    )
}
