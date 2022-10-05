package com.anandarh.storyapp.repositories

import android.content.Context
import com.anandarh.storyapp.services.ApiService

class AuthRepository(private val apiService: ApiService) {
    fun login(email: String, password: String) = apiService.login(email, password)
}