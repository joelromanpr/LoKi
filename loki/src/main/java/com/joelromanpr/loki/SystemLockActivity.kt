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
import android.app.KeyguardManager
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SystemLockActivity : AppCompatActivity() {

    companion object {
        fun launch(a: Activity) {
            a.startActivity(Intent(a, SystemLockActivity::class.java))
        }
    }

    private lateinit var keyguardManager: KeyguardManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
        setContentView(R.layout.activity_system_lock)
        with(findViewById<Button>(R.id.try_again_button)) {
            setOnClickListener { showAuthScreen() }
        }
        checkIfSecurityIsTurnedOff()
        showAuthScreen()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CONFIRM_CREDENTIAL && resultCode == RESULT_OK) {
            PasscodeManager.refreshTimeStamp(this)
            finish()
        }
    }

    private fun showAuthScreen() {
        val i = keyguardManager.createConfirmDeviceCredentialIntent(LokiConfig.appName, null)
        if (i != null) startActivityForResult(i, CONFIRM_CREDENTIAL)
    }

    private fun checkIfSecurityIsTurnedOff() {
        if (keyguardManager.isKeyguardSecure.not()) {
            PasscodeManager.setFeatureEnabled(this, enable = false)
            finish()
            return
        }
    }
}