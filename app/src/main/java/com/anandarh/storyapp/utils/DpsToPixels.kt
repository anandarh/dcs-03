package com.anandarh.storyapp.utils

import android.content.Context

class DpsToPixels(context: Context) {

    private val ctx = context;

    fun getPixels(dps: Int): Int {
        val scale = ctx.resources.displayMetrics.density
        return (dps * scale + 0.5f).toInt()
    }
}