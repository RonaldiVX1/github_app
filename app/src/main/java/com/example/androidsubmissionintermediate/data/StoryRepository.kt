package com.example.androidsubmissionintermediate.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.androidsubmissionintermediate.data.api.ApiService
import com.example.androidsubmissionintermediate.data.response.story.Story

class StoryRepository(private val apiService: ApiService) {
    fun getStories(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }

}