package com.example.storyappdicoding.model.data.response

import com.google.gson.annotations.SerializedName

data class StoryResponse(

	@field:SerializedName("listStory")
	val listStory: List<ListStoryItem>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class AddNewStoryResponse(
	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class StoryDetailResponse(
	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("story")
	val story: ListStoryItem
)

data class ListStoryItem(

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("lon")
	val lon: Any? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("lat")
	val lat: Any? = null
)
