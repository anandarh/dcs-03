package com.anandarh.storyapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.anandarh.storyapp.databinding.ActivityMainBinding
import com.anandarh.storyapp.utils.DataState
import com.anandarh.storyapp.utils.SessionManager
import com.anandarh.storyapp.viewmodels.ListStoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var btLogout: Button

    @Inject
    lateinit var sessionManager: SessionManager

    private val viewModel by viewModels<ListStoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btLogout = binding.logout

        btLogout.setOnClickListener {
            sessionManager.clearSession()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        subscribeUI()
    }

    private fun subscribeUI() {
        viewModel.storiesState.observe(this) { result ->
            when (result) {
                is DataState.Loading -> Log.d("OkHttp", "Loading...")
                is DataState.Success -> Log.d("OkHttp", result.data.toString())
                is DataState.Error -> Log.d("OkHttp", result.exception.toString())
            }
        }
    }
}