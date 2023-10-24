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


class EmomTimerActivity : ComponentActivity() {

    // The total rounds performed
    private var rounds: Int = 0

    private var workSeconds: Int = 0
    private var workRound: Int = 0
    private var restSeconds: Int = 0

    private var isRunning: Boolean = false

    private var workIntervalRepeats: Int = 1
    private var workInterval: Int = 15
    private var restInterval: Int = 15
    private val intervalValues = arrayOf(
        "10", "15", "20", "30", "45", "60", "90", "120", "180", "240", "300"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.emom_timer_activity)

        val btm = findViewById<Button>(R.id.backToMain)
        btm.setOnClickListener {
            isRunning = false
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        setWorkRoundsPicker()
        setWorkIntervalPicker()
        setRestIntervalPicker()
        timer()
    }

    private fun setWorkRoundsPicker() {
        val numberPicker = findViewById<NumberPicker>(R.id.number_picker)

        if (numberPicker != null) {
            numberPicker.wrapSelectorWheel = false

            numberPicker.minValue = 0
            numberPicker.maxValue = 10

            numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
                workIntervalRepeats = newVal
            }
        }
    }

    private fun setWorkIntervalPicker() {
        val numberPicker = findViewById<NumberPicker>(R.id.number_picker)

        if (numberPicker != null) {
            numberPicker.wrapSelectorWheel = false

            numberPicker.minValue = 0
            numberPicker.maxValue = intervalValues.size - 1
            numberPicker.displayedValues = intervalValues

            numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
                workInterval = intervalValues[newVal].toInt()
            }
        }
    }

    private fun setRestIntervalPicker() {
        val numberPicker = findViewById<NumberPicker>(R.id.number_picker)

        if (numberPicker != null) {
            numberPicker.wrapSelectorWheel = false

            numberPicker.minValue = 0
            numberPicker.maxValue = intervalValues.size - 1
            numberPicker.displayedValues = intervalValues

            numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
                restInterval = intervalValues[newVal].toInt()
            }
        }
    }

    fun onClickStart(view: View) {
        workSeconds = workInterval
        rounds++
        updateRoundsView()
        isRunning = true
    }

    fun onClickStop(view: View) {
        workSeconds = 0
        rounds = 0
        updateRoundsView()
        isRunning = false
    }

    private fun secondsToString(): String {
        val hours = (workSeconds / 3600f).toInt()
        val minutes = ((workSeconds % 3600f) / 60f).toInt()
        val secs = (workSeconds % 60f).toInt()
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
                    if (workSeconds > 0) {
                        workSeconds--
                    } else {
                        workSeconds = workInterval
                        if (workRound == workIntervalRepeats) {
                            workRound = 0
                            rounds++
                        } else {
                            workSeconds == workInterval
                            workRound++
                        }
                        updateRoundsView()
                        beepTimer()
                    }
                }
                handler.postDelayed(this, 1_000)
            }
        })
    }
}