package com.anandarh.storyapp.repositories

import com.anandarh.storyapp.models.ResponseModel
import com.anandarh.storyapp.services.ApiService
import com.anandarh.storyapp.utils.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class StoriesRepository @Inject constructor(private val apiService: ApiService) {
    fun fetchStories(): Flow<DataState<ResponseModel>> = flow {
        emit(DataState.Loading)
        try {
            val result = apiService.fetchStories(1, 10, 0)
            emit(DataState.Success(result))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }.flowOn(Dispatchers.IO)
}