package com.anandarh.storyapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseModel(
    val error: Boolean,
    val message: String,
    val loginResult: LoginModel?,
    val listStory: ArrayList<StoryModel>?
) : Parcelable

