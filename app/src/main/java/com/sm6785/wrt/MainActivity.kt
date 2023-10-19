package com.sm6785.wrt

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import java.util.Locale
import kotlin.math.roundToLong

class MainActivity : ComponentActivity() {

    private var seconds:Long = 0L
    private var isRunning: Boolean = false
    private var wasRunning: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState != null) {
            seconds = savedInstanceState.getLong("seconds")
            isRunning = savedInstanceState.getBoolean("running")
            wasRunning = savedInstanceState.getBoolean("wasRunning")
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putLong("seconds", seconds)
        savedInstanceState.putBoolean("running", isRunning)
        savedInstanceState.putBoolean("wasRunning", wasRunning)
    }

    override fun onPause() {
        super.onPause()
        wasRunning = isRunning
        isRunning = false
    }

    override fun onResume() {
        super.onResume()
        if (wasRunning) {
            isRunning = true
        }
    }

    fun onClickStart(view: View) {
        isRunning = true
        runTimer()
    }

    fun onClickStop(view: View) {
        isRunning = false
    }

    fun onClickReset(view: View) {
        isRunning = false
        seconds = 0L
    }

    fun onClickRestDouble(view: View) {
        isRunning = false
        seconds *= 2L
        runRestTimer()
    }
    fun onClickRestOneHalf(view: View) {
        isRunning = false
        seconds *= 1.5f.roundToLong()
        runRestTimer()
    }
    fun onClickRestOne(view: View) {
        isRunning = false
        seconds *= 1L
        runRestTimer()
    }

    private fun secondsToString(): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs)
    }

    private fun runTimer() {
        val timeView: TextView = findViewById(R.id.time_view)
        Handler(Looper.getMainLooper()).postDelayed({
            var time = secondsToString()
            if (isRunning) {
                timeView.text = time
                seconds++
            }}, 1000)
    }

    private fun runRestTimer(){
        val timeView: TextView = findViewById(R.id.time_view)
        object : CountDownTimer(seconds*1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var time = secondsToString()
                timeView.text = time
            }

            override fun onFinish() {
                timeView.text = ("Done!")
            }
        }.start()
    }
}