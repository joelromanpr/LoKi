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
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.joelromanpr.loki.PasscodeManager
import com.joelromanpr.loki.R
import com.joelromanpr.loki.listeners.PasscodeInputListener
import com.joelromanpr.loki.utils.ONE_SECOND_MILLIS
import java.util.ArrayList

class PasscodeSetupActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_REQUIRE_OLD_PIN = "require_old_pin"

        @JvmOverloads
        fun launch(a: Activity, requireOldPin: Boolean = false) {
            val i = Intent(a, PasscodeSetupActivity::class.java)
            i.putExtra(EXTRA_REQUIRE_OLD_PIN, requireOldPin)
            a.startActivity(i)
        }
    }

    private lateinit var passcodeView: PasscodeView
    private var input: List<Int>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passcode_setup)
        passcodeView = findViewById(R.id.passcode_view)
        passcodeView.init()

        val requireOldPin = intent.getBooleanExtra(EXTRA_REQUIRE_OLD_PIN, false) && PasscodeManager.isLocalPasscodeAvailable(this)
        if (requireOldPin) {
            passcodeView.setTitle(R.string.enter_your_old_pin)
            passcodeView.setPasscodeInputListener(ExistingPasscodeInputListener())
        } else {
            passcodeView.setTitle(R.string.set_your_pin_code)
            passcodeView.setPasscodeInputListener(NewPasscodeInputListener())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        this.setResult(RESULT_CANCELED)
        PasscodeManager.refreshTimeStamp(this)
        super.onBackPressed()
    }

    private inner class NewPasscodeInputListener : PasscodeInputListener() {
        override fun onInputFinish(passcode: List<Int>) {
            when {
                input == null -> {
                    input = ArrayList(passcode)
                    passcodeView.setTitle(R.string.verify_your_pin_code)
                    passcodeView.clear()
                }
                passcode == input -> {
                    PasscodeManager.setFeatureEnabled(this@PasscodeSetupActivity, enable = true)
                    PasscodeManager.setPasscodeLock(this@PasscodeSetupActivity, passcode)
                    PasscodeManager.refreshTimeStamp(this@PasscodeSetupActivity)
                    setResult(RESULT_OK)
                    finish()
                }
                else -> {
                    input = null
                    passcodeView.shake()
                    passcodeView.postDelayed({
                        passcodeView.setTitle(R.string.set_your_pin_code)
                        passcodeView.clear()
                    }, ONE_SECOND_MILLIS)
                }
            }
        }
    }

    private inner class ExistingPasscodeInputListener : PasscodeInputListener() {
        override fun onInputFinish(passcode: List<Int>) {
            if (PasscodeManager.isPasscodeValid(this@PasscodeSetupActivity, passcode)) {
                passcodeView.setTitle(R.string.set_new_pin_code)
                passcodeView.setPasscodeInputListener(NewPasscodeInputListener())
                passcodeView.clear()
            } else {
                passcodeView.shake()
                passcodeView.postDelayed({
                    passcodeView.clear()
                }, ONE_SECOND_MILLIS)
            }
        }
    }
}