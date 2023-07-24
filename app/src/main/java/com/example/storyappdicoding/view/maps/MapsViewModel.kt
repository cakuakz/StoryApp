package com.example.storyappdicoding.view.maps

import androidx.lifecycle.ViewModel
import com.example.storyappdicoding.model.data.repository.AuthRepository
import com.example.storyappdicoding.model.data.repository.StoryRepository
import com.example.storyappdicoding.model.data.response.StoryResponse
import kotlinx.coroutines.flow.Flow


class MapsViewModel(private val story : StoryRepository, private val userRepository: AuthRepository): ViewModel() {

    fun getAllStoriesWithLoc(
        page: Int?,
        size: Int?,
        location: Int = 0,
        authorization: String
    ): Flow<Result<StoryResponse>> {
        return story.getAllStories(page, size, location, authorization)
    }

    fun getUserToken(): Flow<String?> {
        return userRepository.getToken()
    }
}