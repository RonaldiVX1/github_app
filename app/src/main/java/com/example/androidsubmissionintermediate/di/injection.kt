package com.example.androidsubmissionintermediate.di

import android.content.Context
import com.example.androidsubmissionintermediate.data.StoryRepository
import com.example.androidsubmissionintermediate.data.api.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService(context)
        return StoryRepository(apiService)
    }
}