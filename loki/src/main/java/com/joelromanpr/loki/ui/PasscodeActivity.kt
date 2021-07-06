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

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.joelromanpr.loki.Loki
import com.joelromanpr.loki.PasscodeManager
import com.joelromanpr.loki.R
import com.joelromanpr.loki.listeners.PasscodeInputListener
import com.joelromanpr.loki.utils.HOUR_IN_MILLIS
import com.joelromanpr.loki.utils.MINUTE_IN_MILLIS
import com.joelromanpr.loki.utils.ONE_SECOND_MILLIS
import com.joelromanpr.loki.utils.show
import kotlin.math.ceil

internal class PasscodeActivity : AppCompatActivity() {

    companion object {
        fun launch(a: Activity) {
            a.startActivity(Intent(a, PasscodeActivity::class.java))
        }
    }

    private var attemptsCounter = 0
    private lateinit var disabledLayout: View
    private lateinit var passcodeView: PasscodeView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passcode)
        setupToolbar()
        disabledLayout = findViewById(R.id.layout_disable)
        passcodeView = findViewById(R.id.passcodeview)
        passcodeView.init()

        passcodeView.setPasscodeInputListener(object : PasscodeInputListener() {
            override fun onInputFinish(passcode: List<Int>) {
                if (PasscodeManager.isPasscodeValid(this@PasscodeActivity, passcode)) {
                    setResult(RESULT_OK)
                    attemptsCounter = 0
                    finish()
                } else {
                    passcodeView.shake()
                    passcodeView.clear()
                    attemptsCounter++
                    val now = System.currentTimeMillis()
                    if (attemptsCounter >= Loki.config.maxAttempts) {
                        var nextAvailableAttemptTime = now
                        nextAvailableAttemptTime += MINUTE_IN_MILLIS

                        if (nextAvailableAttemptTime > now) {
                            passcodeView.show(false)
                            disabledLayout.show(true)
                            PasscodeManager.setNextAvailableTryTime(
                                this@PasscodeActivity,
                                nextAvailableAttemptTime
                            )
                            updateAppDisableTimer(nextAvailableAttemptTime - now)
                        }
                    }
                }
            }
        })
    }

    private fun setupToolbar() {
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()
        attemptsCounter = PasscodeManager.getFailAttemptsCounter(this)
        val now = System.currentTimeMillis()
        val timeLeft = PasscodeManager.getNextAvailableTryTime(this) - now

        if (timeLeft < ONE_SECOND_MILLIS) {
            disabledLayout.show(false)
            passcodeView.show(true)
        } else {
            passcodeView.show(false)
            disabledLayout.show(true)
            updateAppDisableTimer(timeLeft)
        }
    }

    override fun onPause() {
        super.onPause()
        PasscodeManager.setFailAttemptsCounter(this, attemptsCounter)
    }

    private fun updateAppDisableTimer(timeLeft: Long) {
        updateTimeDisplay(timeLeft)
        val countDownTimer: CountDownTimer = object : CountDownTimer(
            timeLeft, (MINUTE_IN_MILLIS / 2).toLong()
        ) {
            override fun onTick(millisUntilFinished: Long) {
                updateTimeDisplay(millisUntilFinished)
            }

            override fun onFinish() {
                attemptsCounter = 0
                disabledLayout.show(false)
                passcodeView.show(true)
            }
        }
        countDownTimer.start()
    }

    private fun updateTimeDisplay(timeLeft: Long) {
        val minute = ceil(timeLeft.toDouble() / MINUTE_IN_MILLIS).toInt()
        val hour = ceil(timeLeft.toDouble() / HOUR_IN_MILLIS).toInt()
        val timeString: String = if (minute > 60) {
            "${getString(R.string.try_again_in)} ${
            resources.getQuantityString(
                R.plurals.hour,
                hour,
                hour
            )
            }"
        } else {
            "${getString(R.string.try_again_in)} ${
            resources.getQuantityString(
                R.plurals.minute,
                minute,
                minute
            )
            }"
        }
        val tryAgainButton = findViewById<TextView>(R.id.try_again_button)
        tryAgainButton.text = timeString
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(Intent.ACTION_MAIN)
        with(intent) {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }
}
