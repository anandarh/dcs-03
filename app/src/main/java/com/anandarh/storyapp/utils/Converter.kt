package com.anandarh.storyapp.utils

import android.content.Context
import com.anandarh.storyapp.models.ResponseModel
import com.google.gson.Gson

fun exceptionResponse(exception: Exception): ResponseModel {
    return Gson().fromJson(exception.message.toString(), ResponseModel::class.java)
}

fun dpsToPixels(context: Context, dps: Int): Int {
    val scale = context.resources.displayMetrics.density
    return (dps * scale + 0.5f).toInt()
}
