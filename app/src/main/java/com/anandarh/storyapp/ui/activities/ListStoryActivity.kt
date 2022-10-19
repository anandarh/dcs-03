package com.anandarh.storyapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anandarh.storyapp.adapters.StoryAdapter
import com.anandarh.storyapp.databinding.ActivityListStoryBinding
import com.anandarh.storyapp.models.StoryModel
import com.anandarh.storyapp.ui.activities.DetailStoryActivity.Companion.EXTRA_STORY
import com.anandarh.storyapp.utils.DataState
import com.anandarh.storyapp.utils.SessionManager
import com.anandarh.storyapp.viewmodels.ListStoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListStoryBinding
    private lateinit var rvStories: RecyclerView
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var btnLogout: AppCompatButton

    @Inject
    lateinit var sessionManager: SessionManager

    private val viewModel by viewModels<ListStoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvStories = binding.rvStories
        btnLogout = binding.btnLogout

        btnLogout.setOnClickListener { logout() }


        subscribeUI()
    }

    private fun logout() {
        sessionManager.clearSession()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun subscribeUI() {
        viewModel.storiesState.observe(this) { result ->
            when (result) {
                is DataState.Loading -> Log.d("OkHttp", "Loading...")
                is DataState.Success -> displayData(result.data.listStory)
                is DataState.Error -> Log.d("OkHttp", result.exception.toString())
            }
        }
    }

    private fun displayData(data: ArrayList<StoryModel>?) {
        if (data != null) {
            storyAdapter = StoryAdapter()
            rvStories.layoutManager = LinearLayoutManager(this)
            rvStories.adapter = storyAdapter
            storyAdapter.addData(data)
            storyAdapter.setOnItemClickListener(object : StoryAdapter.ItemClickListener {
                override fun onItemClick(story: StoryModel) {
                    val intent = Intent(this@ListStoryActivity, DetailStoryActivity::class.java)
                    intent.putExtra(EXTRA_STORY, story)
                    startActivity(intent)
                }
            })
        }
    }
}