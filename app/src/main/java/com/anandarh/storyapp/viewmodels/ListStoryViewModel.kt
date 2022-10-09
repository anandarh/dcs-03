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
import javax.inject.Inject

@HiltViewModel
class ListStoryViewModel @Inject constructor(private val storiesRepository: StoriesRepository) :
    ViewModel() {
    private val _dataState: MutableLiveData<DataState<ResponseModel>> = MutableLiveData()

    val storiesState: LiveData<DataState<ResponseModel>> = _dataState

    init {
        fetchStories()
    }

    private fun fetchStories() {
        viewModelScope.launch {
            storiesRepository.fetchStories().collect {
                _dataState.value = it
            }
        }
    }
}