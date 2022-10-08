package com.anandarh.storyapp.repositories

import com.anandarh.storyapp.models.ResponseModel
import com.anandarh.storyapp.services.ApiService
import com.anandarh.storyapp.utils.DataState
import com.anandarh.storyapp.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthRepository @Inject constructor (
    private val apiService: ApiService,
    private val sessionManager: SessionManager,
    ) {
    fun login(email: String, password: String) : Flow<DataState<ResponseModel>> = flow {
        emit(DataState.Loading)
        try {
            val result = apiService.login(email, password)
            sessionManager.setAuthToken(result.loginResult?.token)
            emit(DataState.Success(result))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }.flowOn(Dispatchers.IO)
}