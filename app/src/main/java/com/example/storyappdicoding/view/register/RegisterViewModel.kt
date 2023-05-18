package com.example.storyappdicoding.view.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyappdicoding.model.data.remote.ApiConfig
import com.example.storyappdicoding.model.data.repository.AuthRepository
import com.example.storyappdicoding.model.data.response.GeneralResponse
import com.example.storyappdicoding.view.login.LoginViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response


class RegisterViewModel(private val authRepository: AuthRepository): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _isRegistered = MutableLiveData<Boolean>()
    val isRegistered: LiveData<Boolean> = _isRegistered

    companion object {
        private const val TAG = "RegisterViewModel"
        const val ERROR_INTERNET = "No Connection"
    }

    fun register(name: String, email: String, password: String) = viewModelScope.launch{
        _isLoading.value = true
        _isRegistered.value = false
        _error.value = null

        val client = ApiConfig.getApiService().register(name, email, password)
        client.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {
                _isRegistered.value = true
                _isLoading.value = false
                if (response.isSuccessful) {
                    Log.d(TAG, "${_isRegistered.value}")
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                if (t is HttpException) {
                    val errorResponse = t.response()?.errorBody()?.string()
                    val errorMessage = errorResponse?.let { JSONObject(it).getString("message") }
                    _error.value = errorMessage
                } else {
                    _error.value = ERROR_INTERNET
                }
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}