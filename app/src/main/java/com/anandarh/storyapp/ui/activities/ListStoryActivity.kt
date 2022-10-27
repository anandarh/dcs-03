package com.anandarh.storyapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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
class ListStoryActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityListStoryBinding
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private var refresh: Boolean = false
    private var isLoading: Boolean = false

    @Inject
    lateinit var sessionManager: SessionManager

    private val viewModel by viewModels<ListStoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogout.setOnClickListener { logout() }
        binding.fabAdd.setOnClickListener { goToAddStory() }
        binding.srStories.setOnRefreshListener(this)

        storyAdapter = StoryAdapter(arrayListOf())
        layoutManager = LinearLayoutManager(this)

        binding.rvStories.layoutManager = layoutManager
        binding.rvStories.adapter = storyAdapter
        binding.rvStories.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading) {
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == storyAdapter.itemCount - 1) {
                        //bottom of list!
                        refresh = false
                        viewModel.fetchStories(refresh)
                    }
                }
            }
        })

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

    private val addStoryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                refresh = true
                binding.srStories.isRefreshing = refresh
                viewModel.fetchStories(refresh)
            }
        }

    private fun subscribeUI() {
        viewModel.storiesState.observe(this) { result ->
            when (result) {
                is DataState.Loading -> handleLoading(true)
                is DataState.Success -> handleSuccess(result.data.listStory)
                is DataState.Error -> handleError(result.exception)
            }
        }
    }

    private fun handleLoading(show: Boolean) {
        isLoading = show
        if (show)
            storyAdapter.addLoading()
        else
            storyAdapter.removeLoading()
    }

    private fun handleSuccess(data: ArrayList<StoryModel>?) {
        handleLoading(false)
        if (data != null) {
            storyAdapter.addData(data, refresh)

            storyAdapter.setOnItemClickListener(object : StoryAdapter.ItemClickListener {
                override fun onItemClick(story: StoryModel) {
                    val intent = Intent(this@ListStoryActivity, DetailStoryActivity::class.java)
                    intent.putExtra(EXTRA_STORY, story)
                    startActivity(intent)
                }
            })

            if (refresh)
                refresh = false

            binding.srStories.isRefreshing = refresh
        }
    }

    private fun handleError(exception: Exception) {
        handleLoading(false)
        Log.d("OkHttp", exception.toString())
    }

    override fun onRefresh() {
        refresh = true
        viewModel.fetchStories(refresh)
    }
}