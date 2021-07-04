package com.joelromanpr.loki.demo

import android.os.Bundle
import com.joelromanpr.loki.LokiActivity
import com.joelromanpr.loki.PasscodeSetupActivity


class MainActivity : LokiActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PasscodeSetupActivity.launch(this)
    }
}