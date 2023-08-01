package com.example.androidsubmissionintermediate.data.response.login

import com.google.gson.annotations.SerializedName

data class LoginResult(
    @SerializedName("name")
    val name: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("userId")
    val userId: String
)