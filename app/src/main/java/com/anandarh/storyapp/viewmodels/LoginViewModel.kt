package com.anandarh.storyapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.anandarh.storyapp.models.ResponseModel
import com.anandarh.storyapp.repositories.AuthRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {
    fun login() {
        repository.login(email = "agus1212@gmail.com", password = "test12345")
            .enqueue(object : Callback<ResponseModel> {
                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    // Error logging in
                }

                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                    val loginResponse = response.body()
                    val errorResponse = Gson().fromJson(response.errorBody()?.charStream(), ResponseModel::class.java)

                    if (loginResponse?.error == false) {
                        Log.d("OkHttp", loginResponse.toString())
                    } else {
                        Log.d("OkHttp", errorResponse.toString())
                    }
                }
            })
    }
}