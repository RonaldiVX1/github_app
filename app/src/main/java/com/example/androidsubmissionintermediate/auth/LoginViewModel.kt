package com.example.androidsubmissionintermediate.auth

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.androidsubmissionintermediate.data.Result
import com.example.androidsubmissionintermediate.data.api.ApiConfig
import com.example.androidsubmissionintermediate.data.response.login.LoginResponse


class LoginViewModel : ViewModel() {

   fun postlogin(email : String, password: String, context: Context) : LiveData<Result<LoginResponse>> = liveData {
       try {
           val response = ApiConfig.getApiService(context).postLogin(email, password)
           emit(Result.Success(response))
       }catch (e: Exception) {
           Log.e("LoginViewModel", "postLogin: ${e.message.toString()}")
           emit(Result.Error(e.message.toString()))
       }
 }

}

