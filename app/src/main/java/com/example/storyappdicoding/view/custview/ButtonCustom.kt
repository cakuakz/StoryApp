package com.example.storyappdicoding.view.custview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.storyappdicoding.R

class ButtonCustom : AppCompatButton {

    private lateinit var enabledButton : Drawable
    private lateinit var disabledButton : Drawable
    private var txtColor: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = if (isEnabled) enabledButton else disabledButton

        setTextColor(txtColor)
        textSize = 12f
        gravity = Gravity.CENTER
        text = if(isEnabled) resources.getString(R.string.button_available) else resources.getString(R.string.button_not_available)
    }

    private fun init(){
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)
        enabledButton = ContextCompat.getDrawable(context, R.drawable.bg_button) as Drawable
        disabledButton = ContextCompat.getDrawable(context, R.drawable.bg_button_disable) as Drawable
    }
}