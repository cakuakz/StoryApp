package com.example.storyappdicoding.view.custview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.storyappdicoding.R

class EditPassword : AppCompatEditText, View.OnTouchListener {

    companion object {
        const val DRAWABLE_RIGHT = 2

    }

    private lateinit var editTextBackground: Drawable
    private lateinit var editTextErrorBackground: Drawable
    private lateinit var hideButtonImage: Drawable
    private lateinit var showButtonImage: Drawable
    private var isVisible = false
    private var isError = false

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

    private fun showButton() {
        setButtonDrawables(endOfTheText = if (isVisible) hideButtonImage else showButtonImage)
    }
    private fun hideButton() {
        setButtonDrawables()
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText:Drawable? = null,
        endOfTheText:Drawable? = null,
        bottomOfTheText: Drawable? = null
    ){
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }


    private fun init() {
        editTextBackground = ContextCompat.getDrawable(context, R.drawable.bg_edit_text) as Drawable
        editTextErrorBackground = ContextCompat.getDrawable(context, R.drawable.bg_edit_text_error) as Drawable
        showButtonImage = ContextCompat.getDrawable(context, R.drawable.baseline_remove_red_eye_24) as Drawable
        hideButtonImage = ContextCompat.getDrawable(context, R.drawable.baseline_visibility_off_24) as Drawable

        setOnTouchListener(this)

        transformationMethod = PasswordTransformationMethod.getInstance()

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) showButton() else hideButton()
                if (s.length < 8) {
                    error = "Password must be 8 character"
                } else {
                    error = null
                }
            }
            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = if (isError) editTextErrorBackground else editTextBackground
    }

    private fun passwordVisibility() {
        isVisible = !isVisible
        transformationMethod = if (isVisible) null else PasswordTransformationMethod.getInstance()
        setButtonDrawables(endOfTheText = if (isVisible) hideButtonImage else showButtonImage)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (compoundDrawables[2] != null) {
            val passwordToggleStart = width - paddingRight - compoundDrawables[2].intrinsicWidth
            if (event != null) {
                if (event.x > passwordToggleStart) {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            passwordVisibility()
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

}