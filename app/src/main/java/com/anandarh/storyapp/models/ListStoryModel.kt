package com.anandarh.storyapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListStoryModel(
    val id: String,
    val name: String,
    val description: String,
    val photo: String,
    val createdAt: String,
    val lat: Double,
    val lon: Double
) : Parcelable