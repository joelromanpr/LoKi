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
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.StringRes
import com.joelromanpr.loki.R
import com.joelromanpr.loki.listeners.PasscodeInputListener

internal class PasscodeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyle, defStyleRes), View.OnClickListener {

    private val pinLength = 4
    private val passcodePinsViews: MutableList<PasscodePinView> = ArrayList(pinLength)
    private val numberButtonViews: MutableList<TextView> = mutableListOf()
    private lateinit var delButtonView: TextView
    private lateinit var titleTextView: TextView
    private lateinit var layoutPasscodePins: View
    private var passcodeInputListener: PasscodeInputListener? = null

    private val code: MutableList<Int> = mutableListOf()

    fun init() {
        val view: View = LayoutInflater.from(context)
            .inflate(R.layout.widget_passcode, this, true)

        passcodePinsViews.add(0, view.findViewById(R.id.pin1))
        passcodePinsViews.add(1, view.findViewById(R.id.pin2))
        passcodePinsViews.add(2, view.findViewById(R.id.pin3))
        passcodePinsViews.add(3, view.findViewById(R.id.pin4))

        numberButtonViews.add(0, view.findViewById(R.id.button0))
        numberButtonViews.add(1, view.findViewById(R.id.button1))
        numberButtonViews.add(2, view.findViewById(R.id.button2))
        numberButtonViews.add(3, view.findViewById(R.id.button3))
        numberButtonViews.add(4, view.findViewById(R.id.button4))
        numberButtonViews.add(5, view.findViewById(R.id.button5))
        numberButtonViews.add(6, view.findViewById(R.id.button6))
        numberButtonViews.add(7, view.findViewById(R.id.button7))
        numberButtonViews.add(8, view.findViewById(R.id.button8))
        numberButtonViews.add(9, view.findViewById(R.id.button9))

        for (i in numberButtonViews.indices) {
            numberButtonViews[i].tag = i
            numberButtonViews[i].setOnClickListener(this)
        }

        delButtonView = view.findViewById(R.id.button_delete)
        delButtonView.setOnClickListener(this)

        titleTextView = view.findViewById(R.id.passcode_input_title_textview)
        layoutPasscodePins = view.findViewById(R.id.layout_passcode_pins)
    }

    override fun onClick(v: View?) {
        v?.let { view ->
            val id = view.id
            if (id == R.id.button_delete) {
                clear()
                return
            }

            val number = v.tag as Int
            if (code.size < pinLength) {
                code.add(number)
                passcodePinsViews[code.size - 1].setActive(true)
            }

            if (code.size == pinLength)
                passcodeInputListener?.onInputFinish(code)
        }
    }

    fun clear() {
        for (pinView in passcodePinsViews) {
            pinView.setActive(false)
        }
        code.clear()
    }

    fun setPasscodeInputListener(listener: PasscodeInputListener) {
        this.passcodeInputListener = listener
    }

    fun shake() {
        this.postDelayed(
            {
                val shakeAnimation = AnimationUtils.loadAnimation(context, R.anim.shake)
                layoutPasscodePins.startAnimation(shakeAnimation)
            },
            250
        )
    }

    fun setTitle(@StringRes title: Int) {
        titleTextView.setText(title)
    }

    fun setTitle(title: String) {
        titleTextView.text = title
    }
}
