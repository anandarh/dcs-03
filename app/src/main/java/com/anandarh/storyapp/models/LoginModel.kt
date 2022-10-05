package com.anandarh.storyapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginModel(
    val userId: String,
    val name: String,
    val token: String
) : Parcelable