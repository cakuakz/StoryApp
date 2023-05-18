package com.example.storyappdicoding.view.detail

import androidx.lifecycle.ViewModel
import com.example.storyappdicoding.model.data.repository.AuthRepository
import com.example.storyappdicoding.model.data.repository.StoryRepository
import com.example.storyappdicoding.model.data.response.StoryDetailResponse
import kotlinx.coroutines.flow.Flow

class DetailViewModel(private val repository: StoryRepository, private val authRepository: AuthRepository): ViewModel() {
    fun getUserDetail(
        id: String,
        authorization: String
    ): Flow<Result<StoryDetailResponse>> {
        return repository.getStoryDetail(id, authorization)
    }

    suspend fun getToken(): Flow<String?> {
        return authRepository.getToken()
    }
}