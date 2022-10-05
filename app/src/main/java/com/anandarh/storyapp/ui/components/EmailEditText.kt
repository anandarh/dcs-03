package com.anandarh.storyapp.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.setPadding
import androidx.core.widget.addTextChangedListener
import com.anandarh.storyapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class EmailEditText : AppCompatEditText, CoroutineScope {

    private lateinit var checkIcon: Drawable
    private lateinit var crossIcon: Drawable

    var isValidEmail : Boolean = false

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

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = "Enter Email"
        setBackgroundResource(R.drawable.background_edit_text)
        setPadding(35)
        textSize = 14f
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
    }

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    private fun init() {;
        checkIcon = ContextCompat.getDrawable(
            context,
            R.drawable.ic_baseline_check_circle_outline
        ) as Drawable
        crossIcon = ContextCompat.getDrawable(
            context,
            R.drawable.ic_baseline_cross_circle_outline
        ) as Drawable
        DrawableCompat.setTint(checkIcon, ContextCompat.getColor(context, R.color.green))
        DrawableCompat.setTint(crossIcon, ContextCompat.getColor(context, R.color.red))

        addTextChangedListener(object : TextWatcher {
            private var searchFor = ""

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                val searchText = s.toString().trim()
                if (searchText == searchFor)
                    return

                searchFor = searchText

                launch {
                    delay(300)  //debounce timeOut
                    if (searchText != searchFor)
                        return@launch

                    validEmail(s.toString())
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                // Do nothing.
            }

        })
    }

    private fun validEmail(text: String) {
        val regex: Regex =
            """(?:[a-z0-9!#${'$'}%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#${'$'}%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])""".toRegex()

        isValidEmail = regex.containsMatchIn(text)

        if (isValidEmail)
            setCompoundDrawablesWithIntrinsicBounds(null, null, checkIcon, null)
        else
            setCompoundDrawablesWithIntrinsicBounds(null, null, crossIcon, null)
    }


}