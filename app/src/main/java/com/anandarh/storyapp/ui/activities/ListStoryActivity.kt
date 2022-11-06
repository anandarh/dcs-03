package com.anandarh.storyapp.ui.activities

import StoryAdapter
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.anandarh.storyapp.adapters.LoadingStateAdapter
import com.anandarh.storyapp.databinding.ActivityListStoryBinding
import com.anandarh.storyapp.utils.SessionManager
import com.anandarh.storyapp.viewmodels.ListStoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ListStoryActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityListStoryBinding
    private lateinit var storyAdapter: StoryAdapter

    @Inject
    lateinit var sessionManager: SessionManager

    private val viewModel by viewModels<ListStoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogout.setOnClickListener { logout() }
        binding.fabAdd.setOnClickListener { goToAddStory() }
        binding.fabMaps.setOnClickListener { goToMaps() }
        binding.srStories.setOnRefreshListener(this)

        storyAdapter = StoryAdapter(this)

        subscribeUI()
    }

    private fun logout() {
        sessionManager.clearSession()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun goToAddStory() {
        addStoryLauncher.launch(Intent(this, PostStoryActivity::class.java))
    }

    private fun goToMaps() {
        startActivity(Intent(this, MapsActivity::class.java))
    }

    private val addStoryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                (binding.rvStories.layoutManager as LinearLayoutManager?)?.scrollToPositionWithOffset(
                    0,
                    0
                )
                storyAdapter.refresh()
            }
        }

    private fun subscribeUI() {
        binding.rvStories.layoutManager = LinearLayoutManager(this)
        binding.rvStories.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }
        )

        viewModel.fetchStories.observe(this) {
            storyAdapter.submitData(lifecycle, it)
            storyAdapter.addLoadStateListener { state ->
                binding.srStories.isRefreshing = state.refresh is LoadState.Loading
            }
        }

        storyAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) {
                    (binding.rvStories.layoutManager as LinearLayoutManager?)?.scrollToPositionWithOffset(
                        0,
                        0
                    )
                }
            }
        })
    }


    override fun onRefresh() {
        storyAdapter.refresh()
    }
}