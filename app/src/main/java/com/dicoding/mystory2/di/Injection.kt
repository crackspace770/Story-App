package com.dicoding.mystory2.di

import android.content.Context
import com.dicoding.mystory2.api.ApiConfig
import com.dicoding.mystory2.data.StoryRepository
import com.dicoding.mystory2.database.StoryDatabase

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database,context,apiService)
    }
}