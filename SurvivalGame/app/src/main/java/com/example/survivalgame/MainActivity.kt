package com.example.survivalgame

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.color.utilities.Score

class MainActivity : AppCompatActivity(),GameTask {
    lateinit var rootLayout: LinearLayout
    lateinit var startBtn: Button
    lateinit var mGameView: GameView
    lateinit var score: TextView
    lateinit var sharedPreferences: SharedPreferences
    private val HIGH_SCORE_KEY = "high_score"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("GamePreferences", Context.MODE_PRIVATE)


        startBtn = findViewById(R.id.startBtn)
        rootLayout = findViewById(R.id.rootLayout)
        score = findViewById(R.id.score)
        mGameView = GameView(this, this)

        val highScore = sharedPreferences.getInt(HIGH_SCORE_KEY, 0)
        score.text = "Highest Score : $highScore"

        startBtn.setOnClickListener {
            score.text = "Score : 0"
            score.visibility = View.VISIBLE
            mGameView.resetGame()
            mGameView.setBackgroundResource(R.drawable.background)
            rootLayout.addView(mGameView)
            startBtn.visibility = View.GONE
            score.visibility = View.GONE


        }



    }

    override fun closeGame(mScore: Int) {
        val highScore = sharedPreferences.getInt(HIGH_SCORE_KEY, 0)
        if (mScore > highScore) {
            sharedPreferences.edit().putInt(HIGH_SCORE_KEY, mScore).apply()
            score.text = "Highest Score : $mScore"
        } else {
            score.text = "Score : $mScore"
        }
        rootLayout.removeView(mGameView)
        startBtn.visibility = View.VISIBLE
        score.visibility = View.VISIBLE
    }

}