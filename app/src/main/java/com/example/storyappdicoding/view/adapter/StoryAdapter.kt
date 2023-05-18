package com.example.storyappdicoding.view.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyappdicoding.model.data.response.ListStoryItem
import com.example.storyappdicoding.model.data.response.StoryResponse
import com.example.storyappdicoding.databinding.ItemStoryBinding
import com.example.storyappdicoding.view.detail.DetailActivity

class StoryAdapter(private val listStory: List<ListStoryItem>) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val storyItem = listStory[position]
        holder.binding.tvName.text = storyItem.name
        Glide.with(holder.itemView.context).load(storyItem.photoUrl).into(holder.binding.userImg)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(storyItem)
        }
    }

    override fun getItemCount() = listStory.size


    class ViewHolder(var binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem)
    }
}
