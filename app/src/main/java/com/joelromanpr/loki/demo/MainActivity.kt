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


package com.joelromanpr.loki.demo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SwitchCompat
import com.joelromanpr.loki.Loki
import com.joelromanpr.loki.LokiConfig
import com.joelromanpr.loki.ui.LokiActivity
import com.joelromanpr.loki.ui.PasscodeSetupActivity

class MainActivity : LokiActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.launch_second_activity).setOnClickListener {
            startActivity(Intent(this, MainActivity2::class.java))
        }
        Loki.overwriteConfig(
            LokiConfig(
                appName = "LoKi Demo",
                maxAttempts = 1,
                android.R.color.holo_blue_dark
            )
        )

        findViewById<SwitchCompat>(R.id.enable_pin).apply {
            this.isChecked = Loki.isEnabled(this@MainActivity)
            this.setOnCheckedChangeListener { _, isChecked ->
                when (isChecked) {
                    true -> PasscodeSetupActivity.launch(this@MainActivity)
                    else -> Loki.disable(this@MainActivity)
                }
            }
        }
    }
}
