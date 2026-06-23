package com.kira.kmp.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Ingredients(
    @SerialName("main")
    val main: List<String>,
    @SerialName("aromatics")
    val aromatics: List<String>,
    @SerialName("liquidsAndSeasonings")
    val liquidsAndSeasonings: List<String>,
    @SerialName("vegetables")
    val vegetables: List<String>,
    @SerialName("optionalAddons")
    val optionalAddons: List<String>,
)