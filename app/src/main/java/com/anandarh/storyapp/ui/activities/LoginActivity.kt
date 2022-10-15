package com.anandarh.storyapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.anandarh.storyapp.R
import com.anandarh.storyapp.databinding.ActivityLoginBinding
import com.anandarh.storyapp.ui.components.CustomEditText
import com.anandarh.storyapp.utils.DataState
import com.anandarh.storyapp.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var emailEditText: CustomEditText
    private lateinit var passwordEditText: CustomEditText
    private lateinit var buttonSignIn: Button
    private lateinit var buttonSignUp: TextView

    private val viewModel by viewModels<LoginViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialization
        emailEditText = binding.edLoginEmail
        passwordEditText = binding.edLoginPassword
        buttonSignIn = binding.btnSignIn
        buttonSignUp = binding.btnSignUp

        buttonSignIn.setOnClickListener { submitSignIn() }
        buttonSignUp.setOnClickListener { gotoRegister() }

        viewModel.loginState.observe(this) { result ->
            when (result) {
                is DataState.Loading -> isLoading(true)
                is DataState.Success -> successHandler()
                is DataState.Error -> errorHandler(result.exception.toString())
            }
        }
    }

    private fun isLoading(boolean: Boolean) {
        if (boolean) {
            buttonSignIn.isEnabled = false
            buttonSignIn.text = this.getText(R.string.loading)
        } else {
            buttonSignIn.isEnabled = true
            buttonSignIn.text = this.getText(R.string.sign_in)
        }
    }

    private fun successHandler() {
        isLoading(false)
        startActivity(Intent(this, ListStoryActivity::class.java))
    }

    private fun errorHandler(error: String) {
        isLoading(false)
        if (error.contains("unauthorized", ignoreCase = true))
            Toast.makeText(this, R.string.invalid_credential, Toast.LENGTH_LONG).show()
        else
            Toast.makeText(this, R.string.something_wrong, Toast.LENGTH_LONG).show()
    }

    private fun submitSignIn() {
        if (!emailEditText.isValid()) {
            Toast.makeText(this, emailEditText.error, Toast.LENGTH_LONG).show()
            return
        } else if (!passwordEditText.isValid()) {
            Toast.makeText(this, passwordEditText.error, Toast.LENGTH_LONG).show()
            return
        }
        viewModel.login(emailEditText.text.toString(), passwordEditText.text.toString())

    }

    private fun gotoRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }


}