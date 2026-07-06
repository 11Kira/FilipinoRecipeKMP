package com.kira.kmp.data.local

import androidx.room.TypeConverter
import com.kira.kmp.model.Ingredients
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromIngredients(value: Ingredients): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toIngredients(value: String): Ingredients {
        return Json.decodeFromString(value)
    }
}
