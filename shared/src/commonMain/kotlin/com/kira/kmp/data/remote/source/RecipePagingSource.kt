package com.kira.kmp.data.remote.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kira.kmp.model.Recipe

class RecipePagingSource(
    private val remoteSource: RecipeRemoteSource,
    private val query: String,
    private val protein: String,
    private val difficulty: String
) : PagingSource<Int, Recipe>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Recipe> {
        val position = params.key ?: 1
        return try {
            val response = remoteSource.getAllRecipes(
                query = query,
                protein = protein,
                difficulty = difficulty,
                page = position
            )
            val recipes = response.data ?: emptyList()
            val nextKey = if (recipes.isEmpty() || response.paging?.next == null) {
                null
            } else {
                position + 1
            }

            LoadResult.Page(
                data = recipes,
                prevKey = if (position == 1) null else position - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Recipe>): Int? {
        return state.anchorPosition
    }
}