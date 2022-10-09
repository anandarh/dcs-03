package com.anandarh.storyapp.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.anandarh.storyapp.databinding.ActivitySplashBinding
import com.anandarh.storyapp.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var sessionManager: SessionManager

    companion object {
        const val DURATION: Long = 1500
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)

        setContentView(binding.root)

        sessionManager = SessionManager(this)

        Handler(Looper.getMainLooper()).postDelayed({

            intent = if (sessionManager.isLoggedIn())
                Intent(this@SplashActivity, MainActivity::class.java)
            else
                Intent(this@SplashActivity, LoginActivity::class.java)

            startActivity(intent)
            finish()

        }, DURATION)
    }
}