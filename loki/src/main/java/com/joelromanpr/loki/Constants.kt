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

internal const val ONE_SECOND_MILLIS = 1000L
internal const val MINUTE_IN_MILLIS = 1000 * 60
internal const val HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60
internal const val CONFIRM_CREDENTIAL = 965 //AD. easter egg for devs ;)
internal const val KEY_LOKI_PASSCODE = "loki_passcode"
internal const val KEY_LOKI_PASSCODE_SALT = "loki_passcode_salt"
internal const val KEY_LOKI_PASSCODE_ACTIVE = "loki_passcode_active"
internal const val KEY_LOKI_PASSCODE_ENABLED = "loki_passcode_enabled"
internal const val KEY_LOKI_PASSCODE_TIME_STAMP = "loki_passcode_time_stamp"
internal const val KEY_LOKI_PASSCODE_VALID_DURATION = "loki_passcode_valid_duration"
internal const val KEY_LOKI_PASSCODE_NEXT_AVAILABLE_TRY_TIME = "loki_passcode_next_available_try_time"
internal const val KEY_LOKI_PASSCODE_FAIL_ATTEMPTS_COUNTER = "loki_passcode_fail_attempts_counter"
