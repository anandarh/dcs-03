package com.anandarh.storyapp.ui.activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.anandarh.storyapp.R
import com.anandarh.storyapp.databinding.ActivityRegisterBinding
import com.anandarh.storyapp.ui.components.BackButton
import com.anandarh.storyapp.ui.components.CustomEditText
import com.anandarh.storyapp.utils.DataState
import com.anandarh.storyapp.utils.exceptionResponse
import com.anandarh.storyapp.viewmodels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var btnBack: BackButton
    private lateinit var btnSignIn: TextView
    private lateinit var btnSignUp: Button
    private lateinit var edName: CustomEditText
    private lateinit var edEmail: CustomEditText
    private lateinit var edPassword: CustomEditText

    private val viewModel by viewModels<RegisterViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnBack = binding.btnBack
        btnSignIn = binding.btnSignIn
        btnSignUp = binding.btnSignUp
        edName = binding.edRegisterName
        edEmail = binding.edRegisterEmail
        edPassword = binding.edRegisterPassword

        btnBack.setOnClickListener { finish() }
        btnSignIn.setOnClickListener { finish() }
        btnSignUp.setOnClickListener { submitSignUp() }


        viewModel.registerState.observe(this) { dataState ->
            when (dataState) {
                is DataState.Loading -> isLoading(true)
                is DataState.Success -> successHandler()
                is DataState.Error -> errorHandler(dataState.exception)
            }
        }
    }

    private fun isLoading(boolean: Boolean) {
        if (boolean) {
            btnSignUp.isEnabled = false
            btnSignUp.text = this.getText(R.string.loading)
        } else {
            btnSignUp.isEnabled = true
            btnSignUp.text = this.getText(R.string.sign_in)
        }
    }

    private fun successHandler() {
        isLoading(false)
        Toast.makeText(this, this.getString(R.string.registration_successful), Toast.LENGTH_LONG)
            .show()
        finish()
    }

    private fun errorHandler(exception: Exception?) {
        isLoading(false)
        val response = exceptionResponse(exception)
        if (response.message.contains("email", true))
            edEmail.setError(response.message)
        Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
    }

    private fun submitSignUp() {
        Log.d("OkHttp", edName.text.toString())
        if (!edName.isValid()) {
            Toast.makeText(this, edName.error, Toast.LENGTH_LONG).show()
            return
        } else if (!edEmail.isValid()) {
            Toast.makeText(this, edEmail.error, Toast.LENGTH_LONG).show()
            return
        } else if (!edPassword.isValid()) {
            Toast.makeText(this, edPassword.error, Toast.LENGTH_LONG).show()
            return
        }

        viewModel.register(
            edName.text.toString(),
            edEmail.text.toString(),
            edPassword.text.toString()
        )
    }
}