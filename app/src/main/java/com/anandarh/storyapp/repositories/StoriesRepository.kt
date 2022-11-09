package com.anandarh.storyapp.repositories

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.anandarh.storyapp.models.ResponseModel
import com.anandarh.storyapp.models.StoryModel
import com.anandarh.storyapp.services.ApiService
import com.anandarh.storyapp.sources.StoryPagingSource
import com.anandarh.storyapp.utils.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class StoriesRepository @Inject constructor(private val apiService: ApiService) {
    fun fetchStories(): LiveData<PagingData<StoryModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }

    fun fetchStories(page: Int, size: Int, location: Int): Flow<DataState<ResponseModel>> = flow {
        emit(DataState.Loading)
        delay(DELAY_RESPONSE)
        try {
            val result = apiService.fetchStories(page, size, location)
            emit(DataState.Success(result))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }.flowOn(Dispatchers.IO)

    fun postStory(
        description: String,
        photo: File,
        lat: Double?,
        lon: Double?
    ): Flow<DataState<ResponseModel>> = flow {
        emit(DataState.Loading)

        val fPhoto = try {
            compressJpeg(photo)
        } catch (e: Exception) {
            photo
        }

        val filePart = MultipartBody.Part.createFormData(
            "photo", fPhoto.name, fPhoto
                .asRequestBody("image/*".toMediaTypeOrNull())
        )

        val fDescription: RequestBody = description
            .toRequestBody("text/plain".toMediaTypeOrNull())

        val fLat: RequestBody = lat.toString()
            .toRequestBody("text/plain".toMediaTypeOrNull())

        val fLon: RequestBody = lon.toString()
            .toRequestBody("text/plain".toMediaTypeOrNull())


        val result = apiService.postStory(
            description = fDescription,
            photo = filePart,
            lat = if (lat != null) fLat else null,
            lon = if (lon != null) fLon else null,
        )

        if (result.isSuccessful) {
            emit(DataState.Success(result.body()!!))
        } else {
            val errorMsg = result.errorBody()?.string()
            result.errorBody()?.close()
            emit(DataState.Error(Exception(errorMsg)))
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun compressJpeg(file: File): File = withContext(Dispatchers.IO) {
        val bitmap = BitmapFactory.decodeFile(file.path)

        var compressQuality = 100
        var streamLength: Int

        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)

        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))

        return@withContext file
    }

    companion object {
        private const val DELAY_RESPONSE = 1000L // millisecond
    }
}