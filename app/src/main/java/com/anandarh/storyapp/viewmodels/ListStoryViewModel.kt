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

    private var currentPage: Int = 1
    private var pagePerSize: Int = 10

    init {
        fetchStories(true)
    }

    fun fetchStories(refresh: Boolean) {
        if (refresh) {
            currentPage = 1
        }
        viewModelScope.launch {
            storiesRepository.fetchStories(currentPage, pagePerSize).collect {
                _dataState.value = it
            }
        }
        currentPage++
    }
}