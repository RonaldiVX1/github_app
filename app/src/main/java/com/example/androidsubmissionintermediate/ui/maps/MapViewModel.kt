package com.example.androidsubmissionintermediate.ui.maps

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.androidsubmissionintermediate.data.Result
import com.example.androidsubmissionintermediate.data.api.ApiConfig
import com.example.androidsubmissionintermediate.data.response.story.StoriesResponse


class MapViewModel() : ViewModel() {

    fun getStoriesWithLocation(context: Context): LiveData<Result<StoriesResponse>> = liveData {
        try {
            val response = ApiConfig.getApiService(context).getStoriesWithLocation(1)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("ListStoryViewModel", "getStoriesWithLocation: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }
}

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}