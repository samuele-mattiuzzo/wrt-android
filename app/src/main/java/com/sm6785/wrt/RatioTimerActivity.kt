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


class RatioTimerActivity : ComponentActivity() {

    private var seconds: Float = 0.0f
    private var isRunning: Boolean = false
    private var isCountdown: Boolean = false
    private var rounds: Int = 0
    private var ratio: Float = 1.0f
    private val ratioValues = arrayOf(
        "1", "1.5", "2", "2.5", "3", "3.5", "4", "4.5", "5"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ratio_timer_activity)

        val btm = findViewById<Button>(R.id.backToMain)
        btm.setOnClickListener {
            isRunning = false
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        setNumberPicker()
        timer()
    }

    private fun setNumberPicker() {
        val numberPicker = findViewById<NumberPicker>(R.id.number_picker)

        if (numberPicker != null) {
            numberPicker.wrapSelectorWheel = false

            numberPicker.minValue = 0
            numberPicker.maxValue = ratioValues.size - 1
            numberPicker.displayedValues = ratioValues

            numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
                ratio = ratioValues[newVal].toFloat()
            }
        }

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

    fun onClickRest(view: View) {
        isRunning = false
        isCountdown = true
        seconds *= ratio
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
                if (isRunning && !isCountdown) {
                    seconds++
                }
                else if (!isRunning && isCountdown && seconds>0f) {
                    seconds--
                }
                else if (isCountdown) {
                    if(seconds == 0f) {
                        isCountdown = false
                        isRunning = true
                        rounds++
                        updateRoundsView()
                        beepTimer()
                    }
                }
                handler.postDelayed(this, 1_000)
            }
        })
    }
}