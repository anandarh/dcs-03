package com.anandarh.storyapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.anandarh.storyapp.BaseApplication
import com.anandarh.storyapp.R
import javax.inject.Inject

/**
 * Session manager to save and fetch data from SharedPreferences
 */
class SessionManager @Inject constructor(context: BaseApplication) {
    private val prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
    }

    /**
     * Function to save auth token
     */
    fun setAuthToken(token: String?) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    /**
     * Function to fetch auth token
     */
    fun getAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    /**
     * Function to fetch auth token
     */
    fun isLoggedIn(): Boolean {
        return !prefs.getString(USER_TOKEN, null).isNullOrBlank()
    }
}