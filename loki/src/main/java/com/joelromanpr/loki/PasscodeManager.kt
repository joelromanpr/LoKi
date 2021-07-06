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

import android.content.Context
import android.preference.PreferenceManager
import com.joelromanpr.loki.utils.KEY_LOKI_PASSCODE
import com.joelromanpr.loki.utils.KEY_LOKI_PASSCODE_ACTIVE
import com.joelromanpr.loki.utils.KEY_LOKI_PASSCODE_ENABLED
import com.joelromanpr.loki.utils.KEY_LOKI_PASSCODE_FAIL_ATTEMPTS_COUNTER
import com.joelromanpr.loki.utils.KEY_LOKI_PASSCODE_NEXT_AVAILABLE_TRY_TIME
import com.joelromanpr.loki.utils.KEY_LOKI_PASSCODE_SALT
import com.joelromanpr.loki.utils.KEY_LOKI_PASSCODE_TIME_STAMP
import com.joelromanpr.loki.utils.KEY_LOKI_PASSCODE_VALID_DURATION
import com.joelromanpr.loki.utils.ONE_SECOND_MILLIS
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import java.security.SecureRandom


internal object PasscodeManager {

    private fun getSharedPrefs(context: Context) =
        PreferenceManager.getDefaultSharedPreferences(context)

    private fun getTimeStamp(context: Context) =
        getSharedPrefs(context).getLong(KEY_LOKI_PASSCODE_TIME_STAMP, 0)

    fun getValidDuration(context: Context) =
        getSharedPrefs(context).getLong(KEY_LOKI_PASSCODE_VALID_DURATION, ONE_SECOND_MILLIS)

    fun setValidDuration(context: Context, duration: Long) = getSharedPrefs(context).edit()
        .putLong(KEY_LOKI_PASSCODE_VALID_DURATION, duration).apply()

    private fun randomString(): String {
        val sRandom = SecureRandom()
        val b = StringBuilder()
        val randomLength = sRandom.nextInt(10)
        var tempChar: Char
        for (i in 0..randomLength) {
            tempChar = (sRandom.nextInt(96) + 32).toChar()
            b.append(tempChar)
        }
        return b.toString()
    }

    fun refreshTimeStamp(context: Context) = getSharedPrefs(context).edit().putLong(
        KEY_LOKI_PASSCODE_TIME_STAMP, System.currentTimeMillis()
    ).apply()

    fun isFeatureEnabled(context: Context) =
        getSharedPrefs(context).getBoolean(KEY_LOKI_PASSCODE_ENABLED, false)

    fun shouldLockScreen(context: Context) = if (isFeatureEnabled(context)) {
        System.currentTimeMillis() - getTimeStamp(context) > getValidDuration(context)
    } else {
        false
    }

    fun setFeatureEnabled(context: Context, enable: Boolean) = getSharedPrefs(context)
        .edit()
        .putBoolean(KEY_LOKI_PASSCODE_ENABLED, enable).apply()

    fun setPasscodeLock(context: Context, passcodeLock: List<Int>) {
        val encoded: String?
        var salt = getSharedPrefs(context).getString(KEY_LOKI_PASSCODE_SALT, null)
        if (salt == null) {
            salt = randomString()
            getSharedPrefs(context).edit().putString(KEY_LOKI_PASSCODE_SALT, salt).apply()
        }

        encoded = if (passcodeLock.isNotEmpty()) {
            val saltAndPasscode = salt + passcodeLock.joinToString(separator = "")
            val shaHexEncode = String(Hex.encodeHex(DigestUtils.sha256(saltAndPasscode)))
            shaHexEncode
        } else
            null

        getSharedPrefs(context)
            .edit()
            .putString(KEY_LOKI_PASSCODE, encoded)
            .apply()

        refreshTimeStamp(context)
    }

    fun isPasscodeValid(context: Context, passcodeLock: List<Int>): Boolean {
        val passcodeLockString = passcodeLock.joinToString(separator = "")
        val salt = getSharedPrefs(context).getString(KEY_LOKI_PASSCODE_SALT, null)
        val encodedCode: String? = if (salt == null) null else {
            val saltAndPasscode = salt + passcodeLockString
            String(Hex.encodeHex(DigestUtils.sha256(saltAndPasscode)))
        }

        val encodedCodeSha1 = String(
            Hex.encodeHex(DigestUtils.sha1(passcodeLockString))
        )

        val original = getSharedPrefs(context).getString(KEY_LOKI_PASSCODE, null)
        if (original == null || original == encodedCode) {
            refreshTimeStamp(context)
            return true
        } else if (original == encodedCodeSha1) {
            setPasscodeLock(context, passcodeLock)
            refreshTimeStamp(context)
            return true
        }

        return false
    }

    fun setPasscodeActive(context: Context, active: Boolean) {
        if (active.not()) refreshTimeStamp(context)

        getSharedPrefs(context).edit().putBoolean(KEY_LOKI_PASSCODE_ACTIVE, active).apply()
    }

    fun setNextAvailableTryTime(context: Context, time: Long) = getSharedPrefs(context).edit()
        .putLong(KEY_LOKI_PASSCODE_NEXT_AVAILABLE_TRY_TIME, time).apply()

    fun getNextAvailableTryTime(context: Context) = getSharedPrefs(context)
        .getLong(KEY_LOKI_PASSCODE_NEXT_AVAILABLE_TRY_TIME, 0)

    fun setFailAttemptsCounter(context: Context, count: Int) = getSharedPrefs(context).edit()
        .putInt(KEY_LOKI_PASSCODE_FAIL_ATTEMPTS_COUNTER, count).apply()

    fun getFailAttemptsCounter(context: Context) = getSharedPrefs(context)
        .getInt(KEY_LOKI_PASSCODE_FAIL_ATTEMPTS_COUNTER, 0)

    fun isLocalPasscodeAvailable(context: Context): Boolean = getSharedPrefs(context)
        .getString(KEY_LOKI_PASSCODE, null)
        .isNullOrEmpty()
        .not()

    fun reset(context: Context) {
        setFeatureEnabled(context, false)
        setPasscodeLock(context, emptyList())
        setFailAttemptsCounter(context, 0)
        refreshTimeStamp(context)
    }

}