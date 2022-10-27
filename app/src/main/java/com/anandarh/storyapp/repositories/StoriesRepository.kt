package com.anandarh.storyapp.repositories

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.anandarh.storyapp.models.ResponseModel
import com.anandarh.storyapp.services.ApiService
import com.anandarh.storyapp.utils.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class StoriesRepository @Inject constructor(private val apiService: ApiService) {
    fun fetchStories(page: Int, size: Int): Flow<DataState<ResponseModel>> = flow {
        emit(DataState.Loading)
        delay(3000)
        try {
            val result = apiService.fetchStories(page, size, 0)
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
            "photo", fPhoto.name, RequestBody.create(
                MediaType.parse("image/*"), fPhoto
            )
        )

        val fDescription: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            description
        )

        val fLat: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            lat.toString()
        )

        val fLon: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            lon.toString()
        )


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
}