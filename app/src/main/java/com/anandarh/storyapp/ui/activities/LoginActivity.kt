package com.anandarh.storyapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.anandarh.storyapp.databinding.ActivityLoginBinding
import com.anandarh.storyapp.ui.components.EmailEditText
import com.anandarh.storyapp.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var emailEditText: EmailEditText
    private lateinit var buttonSignIn: Button
    private lateinit var buttonSignUp: TextView

    private val loginViewModel: LoginViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialization
        emailEditText = binding.edLoginEmail
        buttonSignIn = binding.btnSignIn
        buttonSignUp = binding.btnSignUp

        buttonSignIn.setOnClickListener {submitSignIn()}
        buttonSignUp.setOnClickListener { gotoRegister() }
    }

    private fun submitSignIn() {
        loginViewModel.login()
        if (!emailEditText.isValidEmail) {
            Toast.makeText(this, "Enter a valid email", Toast.LENGTH_LONG).show()
            return
        }
        Toast.makeText(this, "Okay", Toast.LENGTH_LONG).show()

    }

    private fun gotoRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }


}