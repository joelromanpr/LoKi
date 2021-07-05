/*
 * Copyright (c) 2021 joelromanpr (Joel R Sosa)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.joelromanpr.loki.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.joelromanpr.loki.Loki
import com.joelromanpr.loki.utils.convertDpToPixel
import kotlin.math.min

internal class PasscodePinView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyle, defStyleRes) {

    private var viewWidth: Int = -1
    private var viewHeight: Int = -1
    private var active: Boolean = false

    @ColorRes
    private var activePinColor = Loki._config.passcodePinActiveCircleColor


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewWidth = measuredWidth
        viewHeight = measuredHeight
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val paint = Paint()
        with(paint) {
            isAntiAlias = true

            val strokeW: Float = convertDpToPixel(context, 2.0).toFloat()
            val radius = min(viewWidth, viewHeight) / 2 - strokeW

            style = Paint.Style.STROKE
            color = Color.BLACK
            strokeWidth = strokeW
            canvas?.drawCircle(
                radius + strokeW, radius + strokeW, radius, paint
            )
            style = Paint.Style.FILL
            color = if (active)
                ContextCompat.getColor(context, activePinColor)
            else
                Color.WHITE
            canvas?.drawCircle(
                radius + strokeW, radius + strokeW, radius, paint
            )
        }
    }

    fun setActiveColor(@ColorRes color: Int) {
        activePinColor = color
    }

    fun setActive(isActive: Boolean) {
        this.active = isActive
        invalidate()
    }
}