package com.anandarh.storyapp.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.setPadding
import com.anandarh.storyapp.R
import com.anandarh.storyapp.utils.DpsToPixels


class BackButton : AppCompatImageButton {

    private lateinit var dpsToPixels: DpsToPixels

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        dpsToPixels = DpsToPixels(context)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val pixels = dpsToPixels.getPixels(16)

        setImageResource(R.drawable.ic_baseline_arrow_back)
        background = null
        setPadding(pixels)
        minimumWidth = pixels
        minimumHeight = pixels
    }
}