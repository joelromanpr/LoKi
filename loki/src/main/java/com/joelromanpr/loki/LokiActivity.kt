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

import android.app.KeyguardManager
import androidx.appcompat.app.AppCompatActivity


/**
 * Base class for activities that wish to protect its contents with a passcode lock.
 *
 * How to use:
 *      YourActivity : LokiActivity() {}
 * or
 *      YourBaseActivity : LokiActivity() {}
 *
 * Things to note:
 * <ul>
 *      <li> On resume Loki checks if there is a system level passcode is set and launches a
 *      "SystemLock" activity to process. If the previous is not true it checks for the local passcode
 *      instead and launches the "PasscodeLock" activity. If the previous is not true it informs the
 *      PasscodeManager that the feature is not enabled.</li>
 *      <li> On pause Loki refreshes the lock time stamp if needed and sets that the feature is not
 *      active.</li>
 * </ul>
 *
 */
open class LokiActivity : AppCompatActivity() {

    override fun onResume() {
        if (PasscodeManager.shouldLockScreen(this)) {
            val keyGuardMgr = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
            when {
                keyGuardMgr.isKeyguardSecure -> SystemLockActivity.launch(this)
                PasscodeManager.isLocalPasscodeAvailable(this) -> PasscodeActivity.launch(this)
                else -> PasscodeManager.setFeatureEnabled(this, false)
            }
        } else {
            PasscodeManager.setPasscodeActive(this, active = true)
        }
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        PasscodeManager.setPasscodeActive(context = this, active = false)
    }
}