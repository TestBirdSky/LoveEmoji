package com.demolish.penetrating.criticism

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat

class StrokeTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var strokeWidth = 5f // 3dp stroke width
    private var strokeColor = 0xFF000000.toInt() // Black color

    init {
        // Convert dp to pixels
        val density = context.resources.displayMetrics.density
        strokeWidth *= density
    }

    override fun onDraw(canvas: Canvas) {
        // Draw stroke (outline)
        val originalTextColor = currentTextColor
        val originalStyle = paint.style
        val originalStrokeWidth = paint.strokeWidth

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth
        setTextColor(strokeColor)
        super.onDraw(canvas)

        // Draw fill (original text)
        paint.style = Paint.Style.FILL
        paint.strokeWidth = originalStrokeWidth
        setTextColor(originalTextColor)
        super.onDraw(canvas)

        // Restore original style
        paint.style = originalStyle
    }
}
