package com.anandarh.storyapp.ui.components

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.setPadding
import com.anandarh.storyapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class CustomEditText : TextInputLayout, CoroutineScope {

    private val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

    private var textInputEditText: TextInputEditText
    private var inputType : Int = InputType.TYPE_CLASS_TEXT
    private var minLength : Int = 0
    private var maxLength : Int = 0
    private var textHint : String = ""
    private lateinit var checkIcon: Drawable
    private lateinit var crossIcon: Drawable

    private var isValidEmail : Boolean = false
    private var isRequired : Boolean = false

    constructor(context: Context) : super(context) {
        textInputEditText = TextInputEditText(context)
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText)
        inputType = typedArray.getInt(R.styleable.CustomEditText_inputType, InputType.TYPE_CLASS_TEXT)
        isRequired = typedArray.getBoolean(R.styleable.CustomEditText_isRequired, false)
        textHint = typedArray.getString(R.styleable.CustomEditText_setHint) ?: ""
        minLength = typedArray.getInt(R.styleable.CustomEditText_minLength, 0)
        maxLength = typedArray.getInt(R.styleable.CustomEditText_maxLength, 0)
        typedArray.recycle()

        textInputEditText = TextInputEditText(context, attrs)
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        textInputEditText = TextInputEditText(context, attrs, defStyleAttr)
        init()
    }

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    private fun init() {

        endIconMode = if (inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD) END_ICON_PASSWORD_TOGGLE else END_ICON_NONE
        boxStrokeWidth = 0
        boxStrokeWidthFocused = 0
        isHintAnimationEnabled = false
        isHintEnabled = false

        if (inputType == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
            errorIconDrawable = null

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

        textInputEditText.layoutParams = layoutParams
        textInputEditText.background = ContextCompat.getDrawable(context, R.drawable.background_edit_text)
        textInputEditText.textSize = 14f
        textInputEditText.setPadding(35)
        textInputEditText.inputType = inputType+1
        textInputEditText.typeface = ResourcesCompat.getFont(context, R.font.roboto)

        when (inputType) {
            InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS -> textInputEditText.hint = context.getString(R.string.enter_email)
            InputType.TYPE_TEXT_VARIATION_PASSWORD -> textInputEditText.hint = context.getString(R.string.enter_password)
            else -> textInputEditText.hint = textHint
        }

        addView(textInputEditText)

        textInputEditText.addTextChangedListener(object : TextWatcher {
            private var searchFor = ""

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    if ((inputType == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) or (minLength != 0) or (maxLength != 0))  {
                        val searchText = s.toString().trim()
                        if (searchText == searchFor)
                            return

                        searchFor = searchText

                        launch {
                            delay(300)  //debounce timeOut
                            if (searchText != searchFor)
                                return@launch

                            if (inputType == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) {
                                validEmail(searchText)
                            } else {
                                validateLength(searchText)
                            }
                        }
                    }
            }

            override fun afterTextChanged(p0: Editable?) {
                // Do nothing.
            }

        })
    }

    private fun validEmail(text: String) {
        val regex: Regex =
            """(?:[a-z0-9!#${'$'}%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#${'$'}%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+))""".toRegex()

        isValidEmail = regex.containsMatchIn(text)

        error = if (isValidEmail) {
            isErrorEnabled = false
            textInputEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, checkIcon, null)
            null
        } else if (isRequired && text.isBlank()) {
            isErrorEnabled = true
            textInputEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, crossIcon, null)
            context.getString(R.string.required_field_message)
        } else if (text.isNotBlank()) {
            isErrorEnabled = true
            textInputEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, crossIcon, null)
            context.getString(R.string.invalid_emil_format)
        }
        else {
            isErrorEnabled = false
            textInputEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            null
        }
    }

    private fun validateLength(text: String) {
        error = if (minLength != 0 && text.length < minLength && maxLength == 0) {
            isErrorEnabled = true
            context.getString(R.string.min_length_error, minLength)
        } else if (maxLength != 0 && text.length > maxLength && minLength == 0) {
            isErrorEnabled = true
            context.getString(R.string.max_length_error, maxLength)
        } else if ((minLength != 0 && maxLength != 0) && (text.length < minLength || text.length > maxLength)) {
            isErrorEnabled = true
            context.getString(R.string.minmax_length_error, minLength, maxLength)
        } else if (isRequired && text.isBlank()) {
            isErrorEnabled = true
            context.getString(R.string.required_field_message)
        }
        else {
            isErrorEnabled = false
            null
        }
    }

    fun isValid() : Boolean {
        return if (textInputEditText.text.isNullOrBlank()) {
            if (inputType == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) validEmail("") else validateLength("")
            error.isNullOrBlank()
        } else {
            error.isNullOrBlank()
        }
    }

}