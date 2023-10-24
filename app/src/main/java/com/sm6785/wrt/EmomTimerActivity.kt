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
    private var workInterval: Int = 10
    private var restInterval: Int = 0
    private val intervalValues = arrayOf(
        "10", "15", "20", "30", "45", "60", "90", "120", "180", "240", "300"
    )
    private val intervalValuesToDisplay = arrayOf(
        "10s", "15s", "20s", "30s", "45s", "60s", "90s", "2m", "3m", "4m", "5m"
    )
    private val restIntervalValues = arrayOf(
        "0", "10", "15", "20", "30", "45", "60", "90", "120", "180", "240", "300"
    )
    private val restIntervalValuesToDisplay = arrayOf(
        "0", "10s", "15s", "20s", "30s", "45s", "60s", "90s", "2m", "3m", "4m", "5m"
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

            numberPicker.minValue = 1
            numberPicker.maxValue = 10

            numberPicker.setOnValueChangedListener { _, _, newVal ->
                workIntervalRepeats = newVal
            }
        }
    }

    private fun setWorkIntervalPicker() {
        val numberPicker = findViewById<NumberPicker>(R.id.work_rounds_picker)

        if (numberPicker != null) {
            numberPicker.wrapSelectorWheel = false

            numberPicker.minValue = 0
            numberPicker.maxValue = intervalValues.size - 1
            numberPicker.displayedValues = intervalValuesToDisplay

            numberPicker.setOnValueChangedListener { _, _, newVal ->
                workInterval = intervalValues[newVal].toInt()
            }
        }
    }

    private fun setRestIntervalPicker() {
        val numberPicker = findViewById<NumberPicker>(R.id.rest_picker)

        if (numberPicker != null) {
            numberPicker.wrapSelectorWheel = false

            numberPicker.minValue = 0
            numberPicker.maxValue = restIntervalValues.size - 1
            numberPicker.displayedValues = restIntervalValuesToDisplay

            numberPicker.setOnValueChangedListener { _, _, newVal ->
                restInterval = restIntervalValues[newVal].toInt()
            }
        }
    }

    fun onClickStart(view: View) {
        workSeconds = workInterval
        restSeconds = restInterval
        workRound++
        rounds++
        updateRoundsView()
        isRunning = true
    }

    fun onClickStop(view: View) {
        workSeconds = 0
        restSeconds = 0
        workRound = 0
        rounds = 0
        updateRoundsView()
        isRunning = false
    }

    private fun secondsToString(input: Int): String {
        val hours = (input / 3600f).toInt()
        val minutes = ((input % 3600f) / 60f).toInt()
        val secs = (input % 60f).toInt()
        return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs)
    }

    private fun updateTimeView() {
        val timeView: TextView = findViewById(R.id.time_view)
        if (workSeconds >= 0f && restSeconds == restInterval) {
            timeView.text = "[W] ${secondsToString(workSeconds)}"
        } else {
            timeView.text = "[R] ${secondsToString(restSeconds)}"
        }
    }

    private fun updateRoundsView() {
        val roundsView: TextView = findViewById(R.id.rounds_view)
        roundsView.text = "set $workRound/$workIntervalRepeats | round $rounds"
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
                    // should we work out?
                    if (workSeconds > 0) {
                        workSeconds--
                    } else {
                        // have we completed all the work intervals?
                        if (workRound == workIntervalRepeats) {
                            // should we rest?
                            if (restSeconds > 0) {
                                restSeconds--
                            } else {
                                updateTimeView()
                                // next round
                                restSeconds = restInterval
                                workSeconds = workInterval
                                workRound = 1
                                rounds++
                                updateTimeView()
                            }
                        } else {
                            // do we have more intervals in this round? reset
                            workSeconds = workInterval
                            workRound++
                        }
                        updateRoundsView()
                        //beepTimer()
                    }
                }
                handler.postDelayed(this, 1_000)
            }
        })
    }
}