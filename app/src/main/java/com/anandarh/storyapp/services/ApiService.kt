package com.anandarh.storyapp.services

import com.anandarh.storyapp.models.ResponseModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<ResponseModel>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): ResponseModel

    // Add New Story
    @Multipart
    @POST("stories")
    suspend fun createStory(
        @Path("description") description: RequestBody,
        @Path("photo") photo: MultipartBody.Part,
        @Path("lat") lat: RequestBody?,
        @Path("lon") lon: RequestBody?,
    ): ResponseModel

    // Get All Stories
    @GET("stories")
    suspend fun fetchStories(
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("location") location: Int?,
    ): ResponseModel
}