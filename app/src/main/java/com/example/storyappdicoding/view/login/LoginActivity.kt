package com.example.storyappdicoding.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.storyappdicoding.view.custview.ButtonCustom
import com.example.storyappdicoding.view.custview.EditPassword
import com.example.storyappdicoding.databinding.ActivityLoginBinding
import com.example.storyappdicoding.view.custview.EditEmail
import com.example.storyappdicoding.view.dashboard.MainActivity
import com.example.storyappdicoding.view.helper.ViewModelFactory
import com.example.storyappdicoding.view.register.RegisterActivity


class LoginActivity : AppCompatActivity() {

    private lateinit var editEmail: EditEmail
    private lateinit var editPassword : EditPassword
    private lateinit var buttonCustom : ButtonCustom
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private lateinit var tokenUser: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val btnRegister = binding.btRegister
        btnRegister.setOnClickListener {
            val registIntent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(registIntent)
            finish()
        }
        editEmail = binding.edLoginEmail
        editPassword = binding.edLoginPassword
        buttonCustom = binding.btLogin

        viewBar()
        setupLogin()
        playAnimation()
        setMyButtonEnable()

        editPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: Editable) {
            }
        })

        loginViewModel.isLoading.observe(this, {
            showLoading(it)
        })
    }

    private fun setMyButtonEnable() {
        val result = editPassword.text
        buttonCustom.isEnabled = result != null && result.toString().isNotEmpty()
    }

    private fun viewBar(){
        supportActionBar?.hide()
    }

    private fun setupLogin() {
        binding.apply {
            btLogin.setOnClickListener {
                if (!edLoginEmail.error.isNullOrEmpty() || !edLoginPassword.error.isNullOrEmpty()) {
                    when {
                        !edLoginEmail.error.isNullOrEmpty() -> showToast(edLoginEmail.error.toString())
                        !edLoginPassword.error.isNullOrEmpty() -> showToast(edLoginPassword.error.toString())
                    }
                } else {
                    loginViewModel.apply {
                        loginUser(
                            edLoginEmail.text.toString(),
                            edLoginPassword.text.toString()
                        ).also {
                            token.observe(this@LoginActivity) { tokenUser = it }
                            isLoggedIn.observe(this@LoginActivity) { if (it) {
                                Log.d("LoginActivity", "login worked")
                                showToast("Login Succeed")
                                navigateToken(tokenUser)
                            } }
                            error.observe(this@LoginActivity) { if (!it.isNullOrEmpty()) showToast(it) }
                        }
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun navigateToken(token: String) {
        Intent(this@LoginActivity, MainActivity::class.java).also {
            it.putExtra(MainActivity.EXTRA_TOKEN, token)
            startActivity(it)
            finish()
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.logo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = ObjectAnimator.ofFloat(binding.btLogin, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.btRegister, View.ALPHA, 1f).setDuration(500)
        val edEmail = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(500)
        val edPassword = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(500)


        val together = AnimatorSet().apply {
            playTogether(login, signup)
        }


        AnimatorSet().apply {
            playSequentially(edEmail, edPassword, together)
            start()
        }
    }
}