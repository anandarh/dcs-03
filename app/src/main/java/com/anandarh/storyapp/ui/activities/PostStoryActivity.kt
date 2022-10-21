package com.anandarh.storyapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import com.anandarh.storyapp.databinding.ActivityAddStoryBinding
import com.anandarh.storyapp.ui.components.BackButton
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class PostStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var frmSelect: FrameLayout
    private lateinit var ivPhoto: ImageView
    private lateinit var btnCamera: MaterialButton
    private lateinit var btnGallery: MaterialButton
    private lateinit var btnPost: AppCompatButton
    private lateinit var etDescription: TextInputEditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeUI()
    }

    private fun initializeUI() {
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }
        binding.btnCamera.setOnClickListener { openCamera() }

        frmSelect = binding.frmSelect
        ivPhoto = binding.ivPhoto
        btnCamera = binding.btnCamera
        btnGallery = binding.btnGallery
        btnPost = binding.btnPost
        etDescription = binding.etDescription
    }

    private fun openCamera() {
        startActivity(Intent(this, CameraActivity::class.java))
    }


}