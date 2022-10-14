package com.anandarh.storyapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anandarh.storyapp.models.ResponseModel
import com.anandarh.storyapp.repositories.AuthRepository
import com.anandarh.storyapp.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {
    private var _dataState: MutableLiveData<DataState<ResponseModel>> = MutableLiveData()

    var registerState: LiveData<DataState<ResponseModel>> = _dataState

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            repository.register(name, email, password).collect {
                _dataState.value = it
            }
        }
    }
}