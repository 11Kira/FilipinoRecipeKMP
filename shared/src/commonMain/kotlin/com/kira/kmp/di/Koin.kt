package com.kira.kmp.di

import com.kira.kmp.data.local.TokenManager
import com.kira.kmp.data.remote.AuthService
import com.kira.kmp.data.remote.RecipeService
import com.kira.kmp.data.remote.UserService
import com.kira.kmp.data.remote.createHttpClient
import com.kira.kmp.data.remote.source.AuthRemoteSource
import com.kira.kmp.data.remote.source.RecipeRemoteSource
import com.kira.kmp.data.remote.source.UserRemoteSource
import com.kira.kmp.data.repository.AuthRepository
import com.kira.kmp.data.repository.RecipeRepository
import com.kira.kmp.data.repository.UserRepository
import com.kira.kmp.domain.usecase.AuthUseCase
import com.kira.kmp.domain.usecase.RecipeUseCase
import com.kira.kmp.domain.usecase.UserUseCase
import com.kira.kmp.features.account.auth.forgotpassword.ForgotPasswordViewModel
import com.kira.kmp.features.account.auth.login.LoginViewModel
import com.kira.kmp.features.account.auth.register.RegisterViewModel
import com.kira.kmp.features.account.profile.ProfileViewModel
import com.kira.kmp.features.recipes.details.RecipeDetailsViewModel
import com.kira.kmp.features.recipes.favorites.FavoriteRecipeListViewModel
import com.kira.kmp.features.recipes.list.RecipeListViewModel
import com.kira.kmp.ui.MainViewModel
import com.kira.kmp.utils.NetworkUtils
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val dataModule = module {
    single { createHttpClient(get()) }
    single { AuthService(get()) }
    single { RecipeService(get()) }
    single { UserService(get()) }

    single { AuthRemoteSource(get()) }
    single { RecipeRemoteSource(get()) }
    single { UserRemoteSource(get()) }

    single { AuthRepository(get()) }
    single { RecipeRepository(get()) }
    single { UserRepository(get()) }

    single { TokenManager(get()) }
    single { NetworkUtils() }
}

val useCaseModule = module {
    factory { AuthUseCase(get()) }
    factory { RecipeUseCase(get()) }
    factory { UserUseCase(get()) }
}

val viewModelModule = module {
    factoryOf(::MainViewModel)
    factoryOf(::LoginViewModel)
    factoryOf(::RegisterViewModel)
    factoryOf(::RecipeListViewModel)
    factoryOf(::RecipeDetailsViewModel)
    factoryOf(::FavoriteRecipeListViewModel)
    factoryOf(::ProfileViewModel)
    factoryOf(::ForgotPasswordViewModel)
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(dataModule, useCaseModule, viewModelModule, platformSettingsModule())
    }

// called by iOS
fun initKoin() = initKoin {}
