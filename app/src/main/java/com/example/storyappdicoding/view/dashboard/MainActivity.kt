package com.example.storyappdicoding.view.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyappdicoding.databinding.ActivityMainBinding
import com.example.storyappdicoding.model.data.response.ListStoryItem
import com.example.storyappdicoding.view.adapter.StoryAdapter
import com.example.storyappdicoding.view.addstory.AddStoryActivity
import com.example.storyappdicoding.view.camera.CameraActivity
import com.example.storyappdicoding.view.detail.DetailActivity
import com.example.storyappdicoding.view.helper.ViewModelFactory
import com.example.storyappdicoding.view.login.LoginActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    }

    companion object {
        const val EXTRA_TOKEN = "extra_token"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupMain()


        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.addStoryBtn.setOnClickListener {
            val cameraIntent = Intent(this@MainActivity, AddStoryActivity::class.java)
            startActivity(cameraIntent)
        }

        binding.logoutLogo.setOnClickListener {
            logoutSetup()
            val logoutIntent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(logoutIntent)
        }

        binding.localizationLogo.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStory.addItemDecoration(itemDecoration)
    }

    private fun setupMain() {
        lifecycleScope.launch {
            mainViewModel.getToken().collect { tokenUser ->
                if (tokenUser !== null) {
                    val token = "Bearer $tokenUser"
                    mainViewModel.getAllStories(
                        null,
                        null,
                        0,
                        token
                    ).collectLatest { result ->
                        if (result.isSuccess) {
                            val storyResponse = result.getOrThrow()
                            setListStory(storyResponse.listStory)
                        } else {
                            showToast("Home Failed: ${result.exceptionOrNull()?.message}")
                        }
                    }
                }
            }
        }
    }

    private fun setupView() {
        supportActionBar?.hide()
    }

    private fun setListStory(listStoryItem: List<ListStoryItem>?) {
        val adapter = StoryAdapter(listStoryItem ?: emptyList())
        binding.rvStory.adapter = adapter

        adapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback {
            override fun onItemClicked(storyItem: ListStoryItem) {
                val detailIntent = Intent(this@MainActivity, DetailActivity::class.java)
                detailIntent.putExtra(DetailActivity.EXTRA_STORY, storyItem.id)
                startActivity(detailIntent)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun logoutSetup() {
        lifecycleScope.launch {
            mainViewModel.logoutUser()
            finish()
        }
    }
}