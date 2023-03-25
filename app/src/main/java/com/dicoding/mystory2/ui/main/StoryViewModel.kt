package com.dicoding.mystory2.ui.main


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.mystory2.data.StoryRepository

class StoryViewModel(storyRepository: StoryRepository) : ViewModel() {

    val userStory: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getUserStory().cachedIn(viewModelScope)

}



