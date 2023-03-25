package com.dicoding.mystory2.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mystory2.R
import com.dicoding.mystory2.databinding.ActivityStoryBinding
import com.dicoding.mystory2.model.UserPreference
import com.dicoding.mystory2.ui.login.LoginActivity
import com.dicoding.mystory2.ui.maps.MapActivity
import com.dicoding.mystory2.ui.add.AddActivity
import com.dicoding.mystory2.viewModel.ViewModelFactory


class StoryActivity : AppCompatActivity() {

    private lateinit var storyBinding: ActivityStoryBinding
    private var sharedPreference: UserPreference? = null
    private lateinit var storyViewModel: StoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        storyBinding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(storyBinding.root)
        storyViewModel = ViewModelProvider(this, ViewModelFactory(this))[StoryViewModel::class.java]
        sharedPreference = UserPreference(this)

        storyBinding.rvStories.layoutManager = LinearLayoutManager(this)

        storyBinding.addButton.setOnClickListener {
            val intent = Intent(this@StoryActivity, AddActivity::class.java)
            startActivity(intent)
        }

        getData()


    }

    private fun getData() {

        val adapter = ListStoryAdapter()
        storyBinding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        storyViewModel.userStory.observe(this) {
            adapter.submitData(lifecycle, it)

        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                sharedPreference?.clearSharedPreference()
                Toast.makeText(this, "Logged Out.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@StoryActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

            R.id.mapStory -> {
                val intent = Intent(this@StoryActivity, MapActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

            }

        }
        return super.onOptionsItemSelected(item)
    }
}
