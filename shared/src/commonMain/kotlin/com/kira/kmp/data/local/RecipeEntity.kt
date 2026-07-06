package com.kira.kmp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kira.kmp.model.Ingredients

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val image: String,
    val estimatedMinutes: Int,
    val difficulty: String,
    val category: String,
    val protein: String,
    val mealTime: String,
    val ingredients: Ingredients,
    val steps: List<String>,
    val cookingTips: List<String>,
    val variations: List<String>,
    val servingSuggestions: List<String>,
    val isFavorited: Boolean,
    val createdAt: String?,
    val updatedAt: String?,
    val published: Boolean
)

fun RecipeEntity.toDomain() = com.kira.kmp.model.Recipe(
    id = id,
    title = title,
    description = description,
    image = image,
    estimatedMinutes = estimatedMinutes,
    difficulty = difficulty,
    category = category,
    protein = protein,
    mealTime = mealTime,
    ingredients = ingredients,
    steps = steps,
    cookingTips = cookingTips,
    variations = variations,
    servingSuggestions = servingSuggestions,
    isFavorited = isFavorited,
    createdAt = createdAt,
    updatedAt = updatedAt,
    published = published
)

fun com.kira.kmp.model.Recipe.toEntity() = RecipeEntity(
    id = id,
    title = title,
    description = description,
    image = image,
    estimatedMinutes = estimatedMinutes,
    difficulty = difficulty,
    category = category,
    protein = protein,
    mealTime = mealTime,
    ingredients = ingredients,
    steps = steps,
    cookingTips = cookingTips,
    variations = variations,
    servingSuggestions = servingSuggestions,
    isFavorited = isFavorited,
    createdAt = createdAt,
    updatedAt = updatedAt,
    published = published
)

