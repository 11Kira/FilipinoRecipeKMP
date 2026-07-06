package com.kira.kmp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kira.kmp.data.remote.source.FavoriteRecipePagingSource
import com.kira.kmp.data.remote.source.UserRemoteSource
import com.kira.kmp.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UserRepository(
    private val userRemoteSource: UserRemoteSource
) {
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