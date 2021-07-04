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

import androidx.annotation.ColorRes

object Loki {

    var config: LokiConfig = LokiConfig()
        private set

    fun overwriteConfig(newConfig: LokiConfig) {
        this.config = newConfig
    }
}

data class LokiConfig(
    val appName: String = "Application",
    val maxAttempts: Int = 4,
    @ColorRes
    val passcodePinActiveCircleColor: Int = android.R.color.holo_green_light
)