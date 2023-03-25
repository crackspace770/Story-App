package com.dicoding.mystory2.ui.main

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoriesResponse(

	@field:SerializedName("listStory")
	val listStory: List<ListStoryItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
) : Parcelable

@Parcelize
@Entity(tableName = "story")
data class ListStoryItem(

	@field:SerializedName("photoUrl")
	val photoUrl: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@PrimaryKey
	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("lon")
	val lon: Double,

	@field:SerializedName("lat")
	val lat: Double
) : Parcelable

