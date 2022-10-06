package com.anandarh.storyapp.services

import com.anandarh.storyapp.models.ResponseModel
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): ResponseModel

    // Add New Story
    @Multipart
    @POST("stories")
    suspend fun stories(
        @Path("description") description: RequestBody,
        @Path("photo") photo: MultipartBody.Part,
        @Path("lat") lat: RequestBody?,
        @Path("lon") lon: RequestBody?,
    ): Call<ResponseModel>

    companion object {
        private lateinit var apiService: ApiService

        operator fun invoke(): ApiService {
            if (!Companion::apiService.isInitialized) {
                apiService = Retrofit.Builder()
                    .baseUrl("https://story-api.dicoding.dev/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }).build())
                    .build()
                    .create(ApiService::class.java)
            }
            return apiService
        }
    }
}