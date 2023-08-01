package com.example.androidsubmissionintermediate.data.api

import com.example.androidsubmissionintermediate.data.response.login.LoginResponse
import com.example.androidsubmissionintermediate.data.response.register.RegisterResponse
import com.example.androidsubmissionintermediate.data.response.story.PostStoryResponse
import com.example.androidsubmissionintermediate.data.response.story.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
   suspend fun getPagingStories(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ) : StoriesResponse


    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?,
    ): PostStoryResponse

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Query("location") location: Int,
    ) : StoriesResponse
}