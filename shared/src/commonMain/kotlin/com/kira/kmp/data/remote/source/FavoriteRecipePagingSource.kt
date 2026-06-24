package com.kira.kmp.data.remote.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kira.kmp.model.Recipe

class FavoriteRecipePagingSource(
    private val remoteSource: UserRemoteSource,
    private val query: String,
) : PagingSource<Int, Recipe>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Recipe> {
        val position = params.key ?: 1
        return try {
            val response = remoteSource.getAllFavoriteRecipes(
                query = query,
                page = position
            )
            val recipes = response.data ?: emptyList()

            LoadResult.Page(
                data = recipes,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (response.paging?.hasNext == true) position + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Recipe>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
