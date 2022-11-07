package com.anandarh.storyapp.sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.anandarh.storyapp.models.StoryModel
import com.anandarh.storyapp.services.ApiService
import com.anandarh.storyapp.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.delay

class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, StoryModel>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryModel> {
        wrapEspressoIdlingResource {
            return try {
                val position = params.key ?: INITIAL_PAGE_INDEX
                delay(1000)
                val responseData = apiService.fetchStories(position, params.loadSize, 0)
                LoadResult.Page(
                    data = responseData.listStory ?: arrayListOf(),
                    prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                    nextKey = if (responseData.listStory.isNullOrEmpty()) null else position + 1
                )
            } catch (exception: Exception) {
                return LoadResult.Error(exception)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StoryModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}