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

object Loki {

    val config: LokiConfig get() = _config
    private var _config: LokiConfig = LokiConfig()

    fun overwriteConfig(newConfig: LokiConfig) {
        this._config = newConfig
    }

    fun isEnabled(context: Context) = PasscodeManager.isFeatureEnabled(context)
    fun disable(context: Context) {
        PasscodeManager.reset(context)
    }
}
