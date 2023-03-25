package com.dicoding.mystory2.ui.main

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.mystory2.databinding.ActivityStoryListBinding
import com.dicoding.mystory2.ui.detail.DetailActivity


class ListStoryAdapter :
    PagingDataAdapter<ListStoryItem, ListStoryAdapter.ListViewHolder>(DIFF_CALLBACK) {

    class ListViewHolder(private var binding: ActivityStoryListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ListStoryItem){
            binding.apply {
                val dateList = data.createdAt.split("T")
                val dateListListStoryItem = dateList[0]
                binding.apply {
                    tvName.setText(data.name)
                    tvDescription.setText(data.description)
                    Glide.with(itemView.context)
                        .load(data.photoUrl)
                        .centerCrop()
                        .into(imgAvatar)
                    tvDate.setText(dateListListStoryItem)
                    val listListStoryItemDetail = ListStoryItem(data.id,data.name,data.description,data.photoUrl,data.createdAt,data.lat,data.lon)
                    Log.d("story:",listListStoryItemDetail.toString())

                    itemView.setOnClickListener {
                        val intent = Intent(itemView.context, DetailActivity::class.java)
                        intent.putExtra(DetailActivity.EXTRA_STORY, data)

                        val optionsCompat: ActivityOptionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                itemView.context as Activity,
                                Pair(imgAvatar,"imageListStoryItem"),
                                Pair(tvName,"nameListStoryItem"),
                                Pair(tvDescription,"nameListStoryItem"),
                                Pair(tvDate,"dateListStoryItem"),
                            )
                        itemView.context.startActivity(intent, optionsCompat.toBundle())
                    }
                }

            }
        }
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
                holder.bind(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val activityStoryListBinding = ActivityStoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(activityStoryListBinding)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }

    }

}

