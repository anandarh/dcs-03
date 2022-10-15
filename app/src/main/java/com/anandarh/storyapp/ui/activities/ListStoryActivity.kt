package com.anandarh.storyapp.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anandarh.storyapp.R
import com.anandarh.storyapp.adapters.StoryAdapter
import com.anandarh.storyapp.databinding.ActivityListStoryBinding
import com.anandarh.storyapp.utils.DataState
import com.anandarh.storyapp.utils.SessionManager
import com.anandarh.storyapp.viewmodels.ListStoryViewModel
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListStoryBinding
    private lateinit var rvStories: RecyclerView
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var bottomNavBar: ChipNavigationBar

    @Inject
    lateinit var sessionManager: SessionManager

    private val viewModel by viewModels<ListStoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvStories = binding.rvStories
        bottomNavBar = binding.bottomNavBar
        bottomNavBar.setItemSelected(R.id.nav_logout)

        storyAdapter = StoryAdapter()

        rvStories.layoutManager = LinearLayoutManager(this)
        rvStories.adapter = storyAdapter


//        btLogout.setOnClickListener {
//            sessionManager.clearSession()
//            startActivity(Intent(this, LoginActivity::class.java))
//            finish()
//        }

        subscribeUI()
    }

    private fun subscribeUI() {
        viewModel.storiesState.observe(this) { result ->
            when (result) {
                is DataState.Loading -> Log.d("OkHttp", "Loading...")
                is DataState.Success -> {
                    storyAdapter.addData(result.data.listStory!!)
                    Log.d("OkHttp", result.data.listStory.toString())
                }
                is DataState.Error -> Log.d("OkHttp", result.exception.toString())
            }
        }
    }
}