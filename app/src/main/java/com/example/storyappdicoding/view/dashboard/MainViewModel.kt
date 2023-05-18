package com.example.storyappdicoding.view.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyappdicoding.model.data.remote.ApiConfig
import com.example.storyappdicoding.model.data.repository.AuthRepository
import com.example.storyappdicoding.model.data.repository.StoryRepository
import com.example.storyappdicoding.model.data.response.ListStoryItem
import com.example.storyappdicoding.model.data.response.StoryResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

@Suppress("UNREACHABLE_CODE")
class MainViewModel(private val repository: StoryRepository, private val auth: AuthRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    companion object {
        private const val TAG = "MainViewModel"
        const val ERROR_INTERNET = "No Connection"
        const val CONNECTED = "Story Showed Up"
    }

    fun getAllStories(
        page: Int?,
        size: Int?,
        location: Int = 0,
        authorization: String
    ): Flow<Result<StoryResponse>> {
        return repository.getAllStories(page, size, location, authorization)
    }

    suspend fun getToken(): Flow<String?> {
        return auth.getToken()
    }

    suspend fun logoutUser() {
        return auth.logoutUser()
    }

}