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

package com.joelromanpr.loki

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.ceil

class PasscodeActivity : AppCompatActivity() {

    companion object {
        fun launch(a: Activity) {
            a.startActivity(Intent(a, PasscodeActivity::class.java))
        }
    }

    private var attemptsCounter = 0
    private lateinit var disabledLayout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passcode)
        disabledLayout = findViewById(R.id.layout_disable)
        val passcodeView = findViewById<PasscodeView>(R.id.passcodeview)

        passcodeView.setPasscodeInputListener(object : PasscodeInputListener() {
            override fun onInputFinish(passcode: List<Int>) {
                if (PasscodeManager.isPasscodeValid(this@PasscodeActivity, passcode)) {
                    setResult(RESULT_OK)
                    attemptsCounter = 0
                    finish()
                } else {
                    passcodeView.shake()
                    attemptsCounter++
                    val now = System.currentTimeMillis()
                    if (attemptsCounter > LokiConfig.maxAttempts) {
                        var nextAvailableAttemptTime = now
                        when {
                            attemptsCounter == 4 -> {
                                nextAvailableAttemptTime += MINUTE_IN_MILLIS
                            }
                            attemptsCounter == 6 -> {
                                nextAvailableAttemptTime += MINUTE_IN_MILLIS * 5
                            }
                            attemptsCounter == 8 -> {
                                nextAvailableAttemptTime += MINUTE_IN_MILLIS * 30
                            }
                            attemptsCounter == 10 -> {
                                nextAvailableAttemptTime += HOUR_IN_MILLIS
                            }
                            attemptsCounter >= 12 -> {
                                nextAvailableAttemptTime += HOUR_IN_MILLIS * 4
                            }
                        }

                        if (nextAvailableAttemptTime > now) {
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

    private fun updateAppDisableTimer(timeLeft: Long) {
        updateTimeDisplay(timeLeft)
        val countDownTimer: CountDownTimer = object : CountDownTimer(
            timeLeft, (MINUTE_IN_MILLIS / 2).toLong()
        ) {
            override fun onTick(millisUntilFinished: Long) {
                updateTimeDisplay(millisUntilFinished)
            }

            override fun onFinish() {
                disabledLayout.show(false)
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
        val tryAgainButton = findViewById<Button>(R.id.try_again_button)
        tryAgainButton.text = timeString
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