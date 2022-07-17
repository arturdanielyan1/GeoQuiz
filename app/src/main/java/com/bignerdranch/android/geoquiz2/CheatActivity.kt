package com.bignerdranch.android.geoquiz2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView

class CheatActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        fun newIntent(context: Context, isAnswerTrue: Boolean) = Intent(context, CheatActivity::class.java).apply {
            putExtra(EXTRA_ANSWER_IS_TRUE, isAnswerTrue)
        }
    }

    private lateinit var answerTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerTextView = findViewById(R.id.answer_tv)
        answerTextView.textSize = 12.toFloat()

        findViewById<Button>(R.id.reveal_answer_button).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.reveal_answer_button -> {
                answerTextView.textSize = 36.toFloat()
                answerTextView.text = "Answer is " + intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false).toString()
                setResult(RESULT_OK, Intent().putExtra(EXTRA_WAS_ANSWER_SHOWN, true))
                Log.d(LOG_TAG, "extra is put")
            }
        }
    }
}