package com.anandarh.storyapp.ui.components

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.anandarh.storyapp.R
import com.anandarh.storyapp.utils.dpsToPixels


class BackButton : AppCompatImageButton {

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
        val pixels = dpsToPixels(context, 16)

        setImageResource(R.drawable.ic_baseline_arrow_back)
        setPadding(pixels)
        minimumWidth = pixels
        minimumHeight = pixels
    }

}