package com.anandarh.storyapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anandarh.storyapp.models.ResponseModel
import com.anandarh.storyapp.repositories.StoriesRepository
import com.anandarh.storyapp.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PostStoryViewModel @Inject constructor(private val repository: StoriesRepository) :
    ViewModel() {
    private val _dataState: MutableLiveData<DataState<ResponseModel>> = MutableLiveData()

    val postState: LiveData<DataState<ResponseModel>> = _dataState

    fun postStory(
        description: String,
        photo: File,
        lat: Double?,
        lon: Double?
    ) {
        viewModelScope.launch {
            repository.postStory(description, photo, lat, lon).collect {
                _dataState.value = it
            }
        }
    }
}