package com.kira.kmp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kira.kmp.data.local.user.UserDao
import com.kira.kmp.data.local.user.toDomain
import com.kira.kmp.data.local.user.toEntity
import com.kira.kmp.data.remote.source.FavoriteRecipePagingSource
import com.kira.kmp.data.remote.source.UserRemoteSource
import com.kira.kmp.model.Recipe
import com.kira.kmp.model.User
import com.kira.kmp.model.enums.ResponseStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UserRepository(
    private val userRemoteSource: UserRemoteSource,
    private val userDao: UserDao
) {
    val userProfileFlow: Flow<User?> = userDao.getUserProfileFlow().map { it?.toDomain() }

    suspend fun refreshUserProfile() = withContext(Dispatchers.IO) {
        val response = userRemoteSource.getUserProfile()
        if (response.status == ResponseStatus.SUCCESS && response.data != null) {
            // Optional: clear any old tenant data first before saving new profile
            userDao.clearUserProfile()
            userDao.insertUserProfile(response.data.toEntity())
        }
        return@withContext response
    }

    suspend fun clearLocalProfile() {
        userDao.clearUserProfile()
    }

    fun getAllFavoriteRecipes(
        query: String,
    ): Flow<PagingData<Recipe>> =
        Pager(
            PagingConfig(
                pageSize = 10,
                prefetchDistance = 2,
                enablePlaceholders = false,
                initialLoadSize = 10
            )
        ) {
            FavoriteRecipePagingSource(
                remoteSource = userRemoteSource,
                query = query,
            )
        }.flow

    suspend fun toggleFavoriteRecipe(
        recipeId: String
    ) = withContext(Dispatchers.IO) {
        userRemoteSource.toggleFavoriteRecipe(
            recipeId
        )
    }

    suspend fun getUserProfile() = withContext(Dispatchers.IO) { userRemoteSource.getUserProfile() }
}