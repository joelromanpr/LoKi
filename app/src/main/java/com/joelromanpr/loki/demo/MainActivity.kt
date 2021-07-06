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