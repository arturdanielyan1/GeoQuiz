package com.bignerdranch.android.geoquiz2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var previousButton: ImageButton

    private lateinit var questionTextView: TextView

    private var currentIndex = 0

//    private var hasCheated = false

    private var answered = 0
    private var rightAnswers = 0

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_QUESTION_INDEX, currentIndex)
        outState.putInt(KEY_RIGHT_ANSWERS, rightAnswers)
        outState.putInt(KEY_ANSWERED, answered)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(LOG_TAG, "onSaveInstanceState: ")

        currentIndex = savedInstanceState?.getInt(KEY_QUESTION_INDEX, currentIndex) ?: currentIndex
        answered = savedInstanceState?.getInt(KEY_ANSWERED, answered) ?: answered
        rightAnswers = savedInstanceState?.getInt(KEY_RIGHT_ANSWERS, rightAnswers) ?: rightAnswers

        questionTextView = findViewById<TextView>(R.id.question_tv).apply { setOnClickListener(this@MainActivity) }
        updateQuestion()

        findViewById<Button>(R.id.cheat_button).apply { setOnClickListener(this@MainActivity) }

        trueButton = findViewById<Button>(R.id.true_button).apply { setOnClickListener(this@MainActivity) }
        falseButton = findViewById<Button>(R.id.false_button).apply { setOnClickListener(this@MainActivity) }

        nextButton = findViewById<ImageButton>(R.id.next_button).apply { setOnClickListener(this@MainActivity) }
        previousButton = findViewById<ImageButton>(R.id.previous_button).apply { setOnClickListener(this@MainActivity) }
    }

    override fun onClick(v: View?) {
        if((v?.id == R.id.true_button || v?.id == R.id.false_button))

        if(!currentQuestion.isAnswered && !currentQuestion.hasCheated) { // on first answering
            answered++
            if(answered == questions.size) {
                if(v.id == R.id.true_button) {
                    if(currentQuestion.isTrue) rightAnswers++
                    Log.d(LOG_TAG, "$rightAnswers $answered")
                    Toast.makeText(this,
                        getString(R.string.right_answers, "${(100*(rightAnswers.toDouble()/answered)).toInt()}%"),
                        Toast.LENGTH_SHORT).show()
                } else {
                    if(!currentQuestion.isTrue) rightAnswers++
                    Toast.makeText(this,
                        getString(R.string.right_answers, "${(100*(rightAnswers.toDouble()/answered)).toInt()}%"),
                        Toast.LENGTH_SHORT).show()
                }
                currentQuestion.isAnswered = true
                return
            }
            if(v.id == R.id.true_button) {
                Toast.makeText(this,
                    if(currentQuestion.isTrue) {rightAnswers++; R.string.correct_toast} else R.string.incorrect_toast,
                    Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this,
                    if(!currentQuestion.isTrue) {rightAnswers++; R.string.correct_toast} else R.string.incorrect_toast,
                    Toast.LENGTH_SHORT).show()
            }
            currentQuestion.isAnswered = true
            return
        }
        else if (!currentQuestion.isAnswered && currentQuestion.hasCheated){ // the case if cheated
            answered++
            Toast.makeText(this, R.string.cheated_toast, Toast.LENGTH_SHORT).show()
            return
        }
        else if(currentQuestion.isAnswered) {// the case if already is answered
            Toast.makeText(this, R.string.answered_toast, Toast.LENGTH_SHORT).show()
            return
        }

        when (v?.id) {
            R.id.question_tv,
            R.id.next_button -> {
                currentIndex++
                updateQuestion()
            }
            R.id.previous_button -> {
                currentIndex--
                updateQuestion()
            }
            R.id.cheat_button -> {
                launcher.launch(CheatActivity.newIntent(this, currentQuestion.isTrue))
            }
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result: ActivityResult? ->
        val hasCheated = result?.data?.getBooleanExtra(EXTRA_WAS_ANSWER_SHOWN, false) ?: false
        currentQuestion.hasCheated = hasCheated
        Log.d(LOG_TAG, hasCheated.toString())
    }

    private fun updateQuestion() {
        if(currentIndex<0) {
            currentIndex = questions.size + currentIndex
        }
        questionTextView.text = getString(questions[currentIndex%Question.questions.size].resource)
    }

    private val questions
        get() = Question.questions

    private val currentQuestion: Question
        get() {
            if(currentIndex<0) {
                currentIndex = questions.size + currentIndex
            }
            return questions[currentIndex%Question.questions.size]
        }
}
