package com.example.storyappdicoding.view.addstory

import androidx.lifecycle.ViewModel
import com.example.storyappdicoding.model.data.repository.AuthRepository
import com.example.storyappdicoding.model.data.repository.StoryRepository
import com.example.storyappdicoding.model.data.response.AddNewStoryResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val repository: StoryRepository, private val authRepository: AuthRepository): ViewModel() {
    suspend fun addNewStory(
        description: RequestBody,
        photo: MultipartBody.Part,
        lat: Float?,
        lon: Float?,
        authHeader: String
    ): Flow<Result<AddNewStoryResponse>> {
        return repository.addNewStory(description, photo, lat, lon, authHeader)
    }

    suspend fun getToken(): Flow<String?> {
        return authRepository.getToken()
    }
}