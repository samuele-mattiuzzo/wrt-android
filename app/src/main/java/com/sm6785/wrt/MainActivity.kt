package com.sm6785.wrt

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.content.Intent
import android.widget.Button

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val btnWrt = findViewById<Button>(R.id.btnWrt)
        btnWrt.setOnClickListener {
            val intent = Intent(this, RatioTimerActivity::class.java)
            startActivity(intent)
        }

        val btnStopwatch = findViewById<Button>(R.id.btnStopwatch)
        btnStopwatch.setOnClickListener {
            val intent = Intent(this, StopwatchTimerActivity::class.java)
            startActivity(intent)
        }

        val btnCountdown = findViewById<Button>(R.id.btnCountdown)
        btnCountdown.setOnClickListener {
            val intent = Intent(this, CountdownTimerActivity::class.java)
            startActivity(intent)
        }

        val btnEmom = findViewById<Button>(R.id.btnEmom)
        btnEmom.setOnClickListener {
            val intent = Intent(this, EmomTimerActivity::class.java)
            startActivity(intent)
        }
    }
}