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
class MapStoriesViewModel @Inject constructor(private val storiesRepository: StoriesRepository) :
    ViewModel() {
    private var _dataState: MutableLiveData<DataState<ResponseModel>> = MutableLiveData()

    val storiesWithLocation: LiveData<DataState<ResponseModel>> = _dataState

    fun fetchStoriesWithLocation() {
        viewModelScope.launch {
            storiesRepository.fetchStories(1, 20, 1).collect {
                _dataState.value = it
            }
        }
    }
}