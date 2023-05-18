package com.example.storyappdicoding.model.di

import android.content.Context
import com.example.storyappdicoding.model.data.local.UserPreferences
import com.example.storyappdicoding.model.data.local.dataStore
import com.example.storyappdicoding.model.data.remote.ApiConfig
import com.example.storyappdicoding.model.data.repository.AuthRepository
import com.example.storyappdicoding.model.data.repository.StoryRepository
import com.example.storyappdicoding.model.utils.AppExecutors


object injection {
    fun provideUserRepository(context: Context): AuthRepository {
        val apiService = ApiConfig.getApiService()
        val userPreference = UserPreferences.getInstance(context.dataStore)
        val appExecutor = AppExecutors()
        return AuthRepository.getInstance(apiService, userPreference, appExecutor)
    }

    fun provideStoryRepository(): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val appExecutor = AppExecutors()
        return StoryRepository.getInstance(apiService, appExecutor)
    }
}