package com.sm6785.wrt

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import java.util.Locale
import kotlin.math.roundToLong


class MainActivity : ComponentActivity() {

    private var seconds: Long = 0L
    private var isRunning: Boolean = false
    private var isCountdown: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState != null) {
            seconds = savedInstanceState.getLong("seconds")
            isRunning = savedInstanceState.getBoolean("running")
            isCountdown = savedInstanceState.getBoolean("isCountdown")
        }
        timer()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putLong("seconds", seconds)
        savedInstanceState.putBoolean("running", isRunning)
        savedInstanceState.putBoolean("isCountdown", isCountdown)
    }


    fun onClickStart(view: View) {
        seconds = 0L
        isRunning = true
    }

    fun onClickStop(view: View) {
        seconds = 0L
        isRunning = false
        isCountdown = false
    }

    fun onClickRestDouble(view: View) {
        isRunning = false
        isCountdown = !isRunning
        seconds *= 2L
    }

    fun onClickRestOneHalf(view: View) {
        isRunning = false
        isCountdown = !isRunning
        seconds *= 1.5f.roundToLong()
    }

    fun onClickRestOne(view: View) {
        isRunning = false
        isCountdown = !isRunning
        seconds *= 1L
    }

    private fun secondsToString(): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs)
    }

    private fun updateTimeView() {
        val timeView: TextView = findViewById(R.id.time_view)
        timeView.text = secondsToString()
    }

    private fun timer() {
        val handler = Handler()
        handler.post(object : Runnable {
            override fun run() {
                if (isRunning && !isCountdown) {
                    seconds++
                }
                else if (!isRunning && isCountdown && seconds>0L) {
                    seconds--
                }
                else if (seconds == 0L) {
                    isCountdown = false
                }
                updateTimeView()
                handler.postDelayed(this, 1_000)
            }
        })
    }
}