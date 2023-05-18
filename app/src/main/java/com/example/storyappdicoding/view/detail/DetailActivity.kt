package com.example.storyappdicoding.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.storyappdicoding.databinding.ActivityDetailBinding
import com.example.storyappdicoding.model.data.response.ListStoryItem
import com.example.storyappdicoding.view.dashboard.MainViewModel
import com.example.storyappdicoding.view.helper.ViewModelFactory
import kotlinx.coroutines.flow.collect

import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(application)
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupDetail()

    }

    private fun setupView() {
        supportActionBar?.hide()
    }

    private fun setupDetail() {

        val storyId = intent.getStringExtra(EXTRA_STORY)

        lifecycleScope.launch {
            detailViewModel.getToken().collect { tokenUser ->
                if (tokenUser !== null && storyId !== null) {
                    val bearer = "Bearer $tokenUser"
                    detailViewModel.getUserDetail(storyId, bearer).collect { result ->
                        if (result.isSuccess) {
                            val storyDetail = result.getOrThrow()
                            setStoryDetail(storyDetail.story)
                        } else {
                            showToast("No token attached")
                        }
                    }
                }
            }
        }
    }

    private fun setStoryDetail(storyDetail: ListStoryItem) {
        Glide.with(this).load(storyDetail.photoUrl).into(binding.userImgDetail)
        binding.usernameDetail.text = storyDetail.name
        binding.descriptionDetail.text = storyDetail.description
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}