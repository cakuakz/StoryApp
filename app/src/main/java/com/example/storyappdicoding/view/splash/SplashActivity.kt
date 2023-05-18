package com.example.storyappdicoding.view.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.storyappdicoding.databinding.ActivitySplashBinding
import com.example.storyappdicoding.view.dashboard.MainActivity
import com.example.storyappdicoding.view.helper.ViewModelFactory
import com.example.storyappdicoding.view.login.LoginActivity
import kotlinx.coroutines.launch



@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val splashViewModel by viewModels<SplashViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupSplash()
    }

    private fun setupView() {
        supportActionBar?.hide()
    }

    private fun setupSplash() {
        lifecycleScope.launch {
            splashViewModel.isLoggedIn().collect { logged ->
                if (logged) {
                    Handler().postDelayed({
                        val intent = Intent(this@SplashActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, 2000)

                } else {
                    Handler().postDelayed({
                        val backIntent = Intent(this@SplashActivity, LoginActivity::class.java)
                        startActivity(backIntent)
                        finish()
                    }, 2000)
                }
            }
        }
    }
}

