package com.example.storyappdicoding.model.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyappdicoding.model.data.local.UserPreferences
import com.example.storyappdicoding.model.data.remote.ApiService
import com.example.storyappdicoding.model.data.response.AddNewStoryResponse
import com.example.storyappdicoding.model.data.response.ListStoryItem
import com.example.storyappdicoding.model.data.response.StoryDetailResponse
import com.example.storyappdicoding.model.data.response.StoryResponse
import com.example.storyappdicoding.model.utils.AppExecutors
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call

class StoryRepository(
    private val apiService: ApiService,
    appExecutors: AppExecutors
) {
    private val diskDispatcher = appExecutors.diskIO.asCoroutineDispatcher()
    private val networkDispatcher = appExecutors.networkIO.asCoroutineDispatcher()

    fun getAllStoriesPaging(
        authorization: String
    ): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, authorization)
            }
        ).liveData
    }

    fun getAllStories(
        page: Int?,
        size: Int?,
        location: Int = 0,
        authorization: String
    ): Flow<Result<StoryResponse>> {
        return flow {
            val response = apiService.getStory(page, size, location, authorization)
            emit(Result.success(response))
        }.catch { throwable ->
            emit(Result.failure(throwable))
        }
    }

    fun getStoryDetail(
        id: String,
        authorization: String
    ): Flow<Result<StoryDetailResponse>> {
        return flow {
            val response = apiService.getStoryDetail(id, authorization)
            emit(Result.success(response))
        }.catch { throwable ->
            emit(Result.failure(throwable))
        }
    }

    suspend fun addNewStory(
        description: RequestBody,
        photo: MultipartBody.Part,
        lat: Float?,
        lon: Float?,
        authHeader: String
    ): Flow<Result<AddNewStoryResponse>> {
        return flow {
            val response = apiService.addNewStory(description, photo, lat, lon, authHeader)
            emit(Result.success(response))
        }.catch { throwable ->
            emit(Result.failure(throwable))
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: StoryRepository? = null
        fun getInstance(apiService: ApiService,appExecutor: AppExecutors): StoryRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = StoryRepository(apiService, appExecutor)
                INSTANCE = instance
                instance
            }
        }
    }
}