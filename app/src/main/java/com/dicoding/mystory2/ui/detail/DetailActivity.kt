package com.dicoding.mystory2.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.mystory2.databinding.ActivityDetailBinding
import com.dicoding.mystory2.ui.main.ListStoryItem


class DetailActivity:AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<ListStoryItem>(EXTRA_STORY) as ListStoryItem
        Log.d("DetailActivity: ",data.toString())
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Glide.with(this)
            .load(data.photoUrl)
            .into(binding.imgStory)
        binding.apply {
            tvDate.setText(data.createdAt)
            tvName.setText(data.name)
            tvDescription.setText(data.description)
        }
        showLoading(false)
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }


}