package com.anandarh.storyapp.ui.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.anandarh.storyapp.databinding.ActivityRegisterBinding
import com.anandarh.storyapp.ui.components.BackButton
import com.anandarh.storyapp.ui.components.CustomEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var btnBack: BackButton
    private lateinit var btnSignIn: TextView
    private lateinit var edName: CustomEditText
    private lateinit var edEmail: CustomEditText
    private lateinit var edPassword: CustomEditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnBack = binding.btnBack
        btnSignIn = binding.btnSignIn
        edName = binding.edRegisterName
        edEmail = binding.edRegisterEmail
        edPassword = binding.edRegisterPassword

        btnBack.setOnClickListener { finish() }
        btnSignIn.setOnClickListener { finish() }
    }
}