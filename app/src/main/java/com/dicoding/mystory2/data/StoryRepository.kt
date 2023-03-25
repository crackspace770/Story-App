package com.dicoding.mystory2.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.*
import com.dicoding.mystory2.api.ApiService
import com.dicoding.mystory2.data.StoryRemoteMediator
import com.dicoding.mystory2.database.StoryDatabase
import com.dicoding.mystory2.ui.main.ListStoryItem

class StoryRepository(private val storyDatabase: StoryDatabase, private val context: Context, private val apiService: ApiService)
{
    @OptIn(ExperimentalPagingApi::class)
    fun getUserStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,

                ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService,context),
            pagingSourceFactory = {
//                QuotePagingSource(apiService)
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }
}