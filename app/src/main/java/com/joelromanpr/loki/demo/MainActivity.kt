package com.joelromanpr.loki.demo

import android.os.Bundle
import android.view.View
import com.joelromanpr.loki.ui.LokiActivity
import com.joelromanpr.loki.ui.PasscodeSetupActivity


class MainActivity : LokiActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.s).let {
            it.setOnClickListener { PasscodeSetupActivity.launch(this@MainActivity) }
        }
    }
}