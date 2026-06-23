package com.kira.kmp.model.enums

import kotlinx.serialization.Serializable

@Serializable
enum class Category {
    QUICK_MEALS,
    FILIPINO_CLASSICS,
    DESSERTS,
    SOUPS,
    SALADS,
    SNACKS
}