package com.example.storyappdicoding.view.splash

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.storyappdicoding.model.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow


class SplashViewModel(private val authRepository: AuthRepository): ViewModel() {
    fun isLoggedIn(): Flow<Boolean> {
        return authRepository.isLoggedin()
    }
}