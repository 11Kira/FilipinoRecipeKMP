package com.kira.kmp.data.local

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.kira.kmp.data.remote.source.RecipeRemoteSource
import com.kira.kmp.model.enums.ResponseStatus

@OptIn(ExperimentalPagingApi::class)
class RecipeRemoteMediator(
    private val remoteSource: RecipeRemoteSource,
    private val recipeDao: RecipeDao,
    private val query: String,
    private val protein: String,
    private val difficulty: String,
) : RemoteMediator<Int, RecipeEntity>() {

    private var currentPage = 1

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RecipeEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    currentPage = 1
                    currentPage
                }

                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    currentPage++
                    currentPage
                }
            }

            val response = remoteSource.getAllRecipes(
                page = page,
                size = state.config.pageSize,
                query = query,
                protein = protein,
                difficulty = difficulty
            )

            val recipes = response.data ?: emptyList()
            val endOfPaginationReached = recipes.isEmpty() || response.paging?.hasNext == false

            if (response.status == ResponseStatus.SUCCESS) {
                if (loadType == LoadType.REFRESH) {
                    recipeDao.deleteAllRecipes()
                }
                recipeDao.insertRecipes(recipes.map { it.toEntity() })
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}