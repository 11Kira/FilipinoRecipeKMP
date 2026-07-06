package com.kira.kmp.di

import com.kira.kmp.data.local.AppDatabase
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
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val dataModule = module {
    single { createHttpClient(get()) }
    // 🌟 Services
    singleOf(::AuthService)
    singleOf(::RecipeService)
    singleOf(::UserService)

    // 🌟 Remote Sources
    singleOf(::AuthRemoteSource)
    singleOf(::RecipeRemoteSource)
    singleOf(::UserRemoteSource)

    // 🌟 Repositories (AuthRepository parameters are resolved implicitly now!)
    singleOf(::AuthRepository)
    singleOf(::RecipeRepository)
    singleOf(::UserRepository)

    // 🌟 Utils & Local storage
    singleOf(::TokenManager)
    singleOf(::NetworkUtils)
    single { get<AppDatabase>().recipeDao() }
    single { get<AppDatabase>().userDao() }
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
        modules(
            dataModule,
            useCaseModule,
            viewModelModule,
            platformSettingsModule(),
            platformDatabaseModule()
        )
    }

// called by iOS
fun initKoin() = initKoin {}
