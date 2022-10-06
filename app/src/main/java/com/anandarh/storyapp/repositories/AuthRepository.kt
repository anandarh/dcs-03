package com.anandarh.storyapp.repositories

import com.anandarh.storyapp.models.ResponseModel
import com.anandarh.storyapp.services.ApiService
import com.anandarh.storyapp.utils.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AuthRepository(private val apiService: ApiService) {
    fun login(email: String, password: String) : Flow<DataState<ResponseModel>> = flow {
        emit(DataState.Loading)
        try {
            val result = apiService.login(email, password)
            emit(DataState.Success(result))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }.flowOn(Dispatchers.IO)
}