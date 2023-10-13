package com.artem.android.quiz

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

private const val TAG = "MainActivity"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {
    private lateinit var trueBtn: Button
    private lateinit var falseBtn: Button
    private lateinit var nextBtn: ImageButton
    private lateinit var prevBtn: ImageButton
    private lateinit var cheatBtn: Button
    private lateinit var questionTextView: TextView

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this)[QuizViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        trueBtn = findViewById(R.id.true_button)
        falseBtn = findViewById(R.id.false_button)
        nextBtn = findViewById(R.id.next_button)
        prevBtn = findViewById(R.id.prev_button)
        cheatBtn = findViewById(R.id.cheat_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueBtn.setOnClickListener{
            checkAnswer(true)
        }
        falseBtn.setOnClickListener {
            checkAnswer(false)
        }
        nextBtn.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }
        prevBtn.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
        }
        cheatBtn.setOnClickListener {
            val intent = CheatActivity.newIntent(this@MainActivity,
                quizViewModel.currentQuestionAnswer)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }
        questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        updateQuestion()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK)
            return
        if (requestCode == REQUEST_CODE_CHEAT)
            quizViewModel.currentQuestionCheated = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG,
            "onStart() called")
    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG,
            "onResume() called")
    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG,
            "onPause() called")
    }
    override fun onStop() {
        super.onStop()
        Log.d(TAG,
            "onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,
            "onDestroy() called")
    }

    private fun updateQuestion() {
        questionTextView.setText(quizViewModel.currentQuestionText)
        if (!quizViewModel.currentQuestionAnswered) {
            trueBtn.isEnabled = true
            falseBtn.isEnabled = true
        }
        else {
            trueBtn.isEnabled = false
            falseBtn.isEnabled = false
        }
    }

    private fun checkAnswer(userAnswer: Boolean) {
        quizViewModel.currentQuestionAnswered = true
        trueBtn.isEnabled = false
        falseBtn.isEnabled = false
        quizViewModel.allAnswers += 1

        if (quizViewModel.currentQuestionCheated)
            Toast.makeText(this, R.string.judgment_toast, Toast.LENGTH_SHORT).show()
        else if (userAnswer == quizViewModel.currentQuestionAnswer) {
            Toast.makeText(this, R.string.correct_toast, Toast.LENGTH_SHORT).show()
            quizViewModel.correctAnswers += 1
        }
        else
            Toast.makeText(this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show()
        if (quizViewModel.allAnswers == quizViewModel.getQuestionsSize()) {
            Toast.makeText(this,
                (quizViewModel.correctAnswers.toDouble()/quizViewModel.allAnswers.toDouble() * 100).toString()
                    + "%", Toast.LENGTH_LONG).show()
        }
    }
}