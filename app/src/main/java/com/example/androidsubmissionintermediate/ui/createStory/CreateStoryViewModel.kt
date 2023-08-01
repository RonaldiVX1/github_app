package com.example.androidsubmissionintermediate.ui.createStory

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.androidsubmissionintermediate.data.Result
import com.example.androidsubmissionintermediate.data.api.ApiConfig
import com.example.androidsubmissionintermediate.data.response.story.PostStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody


class CreateStoryViewModel : ViewModel() {

    fun postStory(imageMultipart: MultipartBody.Part, description: RequestBody, lat : RequestBody?, lon : RequestBody?, context: Context) : LiveData<Result<PostStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = ApiConfig.getApiService(context).postStory(imageMultipart, description, lat, lon)
            emit(Result.Success(response))
        }catch (e: Exception) {
            Log.e("CreateStoryViewModel", "postStory: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }

    }


}
