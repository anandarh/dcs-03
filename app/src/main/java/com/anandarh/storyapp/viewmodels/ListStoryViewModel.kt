package com.anandarh.storyapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.anandarh.storyapp.models.StoryModel
import com.anandarh.storyapp.repositories.StoriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListStoryViewModel @Inject constructor(storiesRepository: StoriesRepository) :
    ViewModel() {
    val fetchStories: LiveData<PagingData<StoryModel>> =
        storiesRepository.fetchStories().cachedIn(viewModelScope)
}