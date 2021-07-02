package com.joelromanpr.loki.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.joelromanpr.loki.LokiActivity

class MainActivity : LokiActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}