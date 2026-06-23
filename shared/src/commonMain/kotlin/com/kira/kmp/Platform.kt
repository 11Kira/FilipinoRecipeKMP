package com.kira.kmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform