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

    private var seconds: Float = 0.0f
    private var isRunning: Boolean = false
    private var isCountdown: Boolean = false
    private var rounds: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState != null) {
            seconds = savedInstanceState.getFloat("seconds")
            isRunning = savedInstanceState.getBoolean("running")
            isCountdown = savedInstanceState.getBoolean("isCountdown")
        }
        timer()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putFloat("seconds", seconds)
        savedInstanceState.putBoolean("running", isRunning)
        savedInstanceState.putBoolean("isCountdown", isCountdown)
    }


    fun onClickStart(view: View) {
        seconds = 0.0f
        rounds++
        updateRoundsView()
        isRunning = true
        isCountdown = !isRunning
    }

    fun onClickStop(view: View) {
        seconds = 0.0f
        rounds = 0
        updateRoundsView()
        isRunning = false
        isCountdown = false
    }

    fun onClickRestDouble(view: View) {
        isRunning = false
        isCountdown = !isRunning
        seconds *= 2.0f
    }

    fun onClickRestOneHalf(view: View) {
        isRunning = false
        isCountdown = !isRunning
        seconds *= 1.5f
    }

    fun onClickRestOne(view: View) {
        isRunning = false
        isCountdown = !isRunning
        seconds *= 1.0f
    }

    private fun secondsToString(): String {
        val hours = (seconds / 3600f).toInt()
        val minutes = ((seconds % 3600f) / 60f).toInt()
        val secs = (seconds % 60f).toInt()
        return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs)
    }

    private fun updateTimeView() {
        val timeView: TextView = findViewById(R.id.time_view)
        timeView.text = secondsToString()
    }

    private fun updateRoundsView() {
        val roundsView: TextView = findViewById(R.id.rounds_view)
        roundsView.text = "Round " + rounds.toString()
    }

    private fun timer() {
        val handler = Handler()
        handler.post(object : Runnable {
            override fun run() {
                updateTimeView()
                if (isRunning && !isCountdown) {
                    seconds++
                }
                else if (!isRunning && isCountdown && seconds>0L) {
                    seconds--
                }
                else if (seconds == 0.0f) {
                    isCountdown = false
                }

                handler.postDelayed(this, 1_000)
            }
        })
    }
}