package com.example.androidsubmissionintermediate.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.androidsubmissionintermediate.data.api.ApiService
import com.example.androidsubmissionintermediate.data.response.story.Story

class StoryPagingSource(private val apiService: ApiService)  : PagingSource<Int, Story>() {

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getPagingStories(position, params.loadSize)
            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (responseData.listStory.isEmpty()) null else position + 1
            )


        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }


}