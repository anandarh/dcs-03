package com.anandarh.storyapp.utils

import android.content.Context
import java.util.*

class LocalizationHelper {
    companion object {

        private lateinit var sessionManager: SessionManager

        @Suppress("DEPRECATION")
        fun setLanguage(context: Context, language: String) {
            sessionManager = SessionManager(context)
            val resource = context.resources
            val configuration = resource.configuration
            val locale = Locale(language)
            Locale.setDefault(locale)
            configuration.setLocale(locale)
            context.createConfigurationContext(configuration)
            resource.updateConfiguration(configuration, resource.displayMetrics)
            sessionManager.setLanguage(language)
        }
    }

}