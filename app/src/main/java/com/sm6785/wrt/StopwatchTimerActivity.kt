package com.sm6785.wrt

import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.activity.ComponentActivity
import java.util.Locale


class StopwatchTimerActivity : ComponentActivity() {

    private var seconds: Float = 0.0f
    private var isRunning: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stopwatch_timer_activity)

        val btm = findViewById<Button>(R.id.backToMain)
        btm.setOnClickListener {
            isRunning = false
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        timer()
    }


    fun onClickStart(view: View) {
        seconds = 0.0f
        isRunning = true
    }

    fun onClickStop(view: View) {
        seconds = 0.0f
        isRunning = false
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
    private fun timer() {
        val handler = Handler()
        handler.post(object : Runnable {
            override fun run() {
                updateTimeView()
                if (isRunning) {
                    seconds++
                }
                handler.postDelayed(this, 1_000)
            }
        })
    }
}