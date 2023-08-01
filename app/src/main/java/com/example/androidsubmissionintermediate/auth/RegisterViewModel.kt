package com.example.androidsubmissionintermediate.auth

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.androidsubmissionintermediate.data.Result
import com.example.androidsubmissionintermediate.data.api.ApiConfig
import com.example.androidsubmissionintermediate.data.api.ApiService
import com.example.androidsubmissionintermediate.data.response.register.RegisterResponse

class RegisterViewModel : ViewModel() {


    fun postRegister(name : String, email : String, password: String, context : Context) : LiveData<Result<RegisterResponse>> = liveData {
        try {
            val response = ApiConfig.getApiService(context).postRegister(name, email, password)
            emit(Result.Success(response))
        }catch (e: Exception) {
            Log.e("LoginViewModel", "postLogin: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }
}