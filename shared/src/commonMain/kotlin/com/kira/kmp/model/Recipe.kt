package com.kira.kmp.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Recipe(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String = "",
    @SerialName("image")
    val image: String,
    @SerialName("estimatedMinutes")
    val estimatedMinutes: Int,
    @SerialName("difficulty")
    val difficulty: String,
    @SerialName("category")
    val category: String,
    @SerialName("protein")
    val protein: String,
    @SerialName("mealTime")
    val mealTime: String,
    @SerialName("ingredients")
    val ingredients: Ingredients,
    @SerialName("steps")
    val steps: List<String>,
    @SerialName("cookingTips")
    val cookingTips: List<String> = emptyList(),
    @SerialName("variations")
    val variations: List<String> = emptyList(),
    @SerialName("servingSuggestions")
    val servingSuggestions: List<String> = emptyList(),
    @SerialName("isFavorited")
    val isFavorited: Boolean,
    @SerialName("createdAt")
    val createdAt: String? = null,
    @SerialName("updatedAt")
    val updatedAt: String? = null,
    @SerialName("published")
    val published: Boolean,
)
