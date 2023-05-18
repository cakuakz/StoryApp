package com.example.storyappdicoding.view.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.storyappdicoding.databinding.ActivityRegisterBinding
import com.example.storyappdicoding.view.custview.ButtonCustom
import com.example.storyappdicoding.view.custview.EditEmail
import com.example.storyappdicoding.view.custview.EditName
import com.example.storyappdicoding.view.custview.EditPassword
import com.example.storyappdicoding.view.dashboard.MainActivity
import com.example.storyappdicoding.view.helper.ViewModelFactory
import com.example.storyappdicoding.view.login.LoginActivity


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var editEmail: EditEmail
    private lateinit var editName: EditName
    private lateinit var editPassword: EditPassword
    private lateinit var buttonCustom: ButtonCustom
    private val registerViewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupRegister()

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

        registerViewModel.isLoading.observe(this, {
            showLoading(it)
        })
    }

    private fun setupView() {
        supportActionBar?.hide()

        editName = binding.registerName
        editEmail = binding.registerEmail
        editPassword = binding.registerPassword
        buttonCustom = binding.btRegister
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun setMyButtonEnable() {
        val result = editPassword.text
        buttonCustom.isEnabled = result != null && result.toString().isNotEmpty()
    }

    private fun setupRegister() {
        binding.btRegister.setOnClickListener {
            if (!binding.registerName.error.isNullOrEmpty() || !binding.registerEmail.error.isNullOrEmpty() || !binding.registerPassword.error.isNullOrEmpty()) {
                when {
                    !binding.registerName.error.isNullOrEmpty() -> showToast(binding.registerName.error.toString())
                    !binding.registerEmail.error.isNullOrEmpty() -> showToast(binding.registerEmail.error.toString())
                    !binding.registerPassword.error.isNullOrEmpty() -> showToast(binding.registerPassword.error.toString())
                }
            } else {
                registerViewModel.apply {
                    register(
                        binding.registerName.text.toString(),
                        binding.registerEmail.text.toString(),
                        binding.registerPassword.text.toString(),
                    ).also {
                        isRegistered.observe(this@RegisterActivity) { if (it) navigateRegister() }
                        error.observe(this@RegisterActivity) { if (!it.isNullOrEmpty()) showToast(it) }
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateRegister() {
        val intentRegister = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intentRegister)
    }

}