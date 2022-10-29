package com.anandarh.storyapp.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.anandarh.storyapp.databinding.ActivitySplashBinding
import com.anandarh.storyapp.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var sessionManager: SessionManager

    companion object {
        const val DURATION: Long = 500
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)

        setContentView(binding.root)

        sessionManager = SessionManager(this)

        Handler(Looper.getMainLooper()).postDelayed({

            if (sessionManager.isLoggedIn()) {
                val intent = Intent(this@SplashActivity, ListStoryActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)

                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this@SplashActivity,
                    binding.tvAppName as View,
                    "app_name"
                )

                startActivity(intent, options.toBundle())
            }

        }, DURATION)

        Handler(Looper.getMainLooper()).postDelayed({
            finish()
        }, DURATION + 500)
    }
}