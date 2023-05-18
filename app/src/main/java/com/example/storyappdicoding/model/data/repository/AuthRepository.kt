package com.example.storyappdicoding.model.data.repository

import com.example.storyappdicoding.model.data.local.UserPreferences
import com.example.storyappdicoding.model.data.remote.ApiService
import com.example.storyappdicoding.model.data.response.GeneralResponse
import com.example.storyappdicoding.model.utils.AppExecutors
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class AuthRepository(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences,
    appExecutors: AppExecutors
) {
    private val diskDispatcher = appExecutors.diskIO.asCoroutineDispatcher()
    private val networkDispatcher = appExecutors.networkIO.asCoroutineDispatcher()

    suspend fun registerUser(name: String, email: String, password: String) = flow {
        emit(
            apiService.register(
                name, email, password
            )
        )
    }.flowOn(networkDispatcher)

    suspend fun loginUser(email: String, password: String) = flow {
        emit(
            apiService.login(
                email, password
            )
        )
    }.flowOn(networkDispatcher)

    suspend fun logoutUser(){
        return withContext(diskDispatcher){
            userPreferences.clearUserToken()
        }
    }

    suspend fun saveToken(token: String) {
        return withContext(diskDispatcher) {
            userPreferences.saveUserToken(token)
        }
    }

    fun getToken(): Flow<String?> {
        return userPreferences.getUserToken()
    }

    fun isLoggedin(): Flow<Boolean> {
        return userPreferences.getUserToken().map { token ->
            token != null
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthRepository? = null
        fun getInstance(apiService: ApiService, userPreference: UserPreferences, appExecutor: AppExecutors): AuthRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = AuthRepository(apiService, userPreference, appExecutor)
                INSTANCE = instance
                instance
            }
        }
    }
}