package com.kira.kmp.model.enums

import kotlinx.serialization.Serializable

@Serializable
enum class Protein {
    BEEF,
    PORK,
    CHICKEN,
    SEAFOOD,
    VEGETABLES,
    NONE
}