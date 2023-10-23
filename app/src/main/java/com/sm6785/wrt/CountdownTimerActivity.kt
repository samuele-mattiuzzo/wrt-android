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


class CountdownTimerActivity : ComponentActivity() {

    private var seconds: Int = 0
    private var isRunning: Boolean = false
    private var rounds: Int = 0
    private var interval: Int = 15
    private val intervalValues = arrayOf(
        "15", "30", "45", "60", "90", "120", "180", "240", "300"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.countdown_timer_activity)

        val btm = findViewById<Button>(R.id.backToMain)
        btm.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds")
            rounds = savedInstanceState.getInt("rounds")
            interval = savedInstanceState.getInt("interval")
            isRunning = savedInstanceState.getBoolean("running")
        }
        setNumberPicker()
        timer()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt("seconds", seconds)
        savedInstanceState.putInt("rounds", rounds)
        savedInstanceState.putInt("interval", interval)
        savedInstanceState.putBoolean("running", isRunning)
    }

    private fun setNumberPicker() {
        val numberPicker = findViewById<NumberPicker>(R.id.number_picker)

        if (numberPicker != null) {
            numberPicker.wrapSelectorWheel = false

            numberPicker.minValue = 0
            numberPicker.maxValue = intervalValues.size - 1
            numberPicker.displayedValues = intervalValues

            numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
                interval = intervalValues[newVal].toInt()
            }
        }
    }


    fun onClickStart(view: View) {
        seconds = interval
        rounds++
        updateRoundsView()
        isRunning = true
    }

    fun onClickStop(view: View) {
        seconds = 0
        rounds = 0
        updateRoundsView()
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

    private fun updateRoundsView() {
        val roundsView: TextView = findViewById(R.id.rounds_view)
        roundsView.text = "Round " + rounds.toString()
    }

    private fun beepTimer() {
        val toneG = ToneGenerator(AudioManager.STREAM_ALARM, 100)
        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 500)
    }

    private fun timer() {
        val handler = Handler()
        handler.post(object : Runnable {
            override fun run() {
                updateTimeView()
                if (isRunning) {
                    if (seconds > 0) {
                        seconds--
                    } else {
                        isRunning = false
                        beepTimer()
                    }
                }
                handler.postDelayed(this, 1_000)
            }
        })
    }
}