package com.artem.android.quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var trueBtn: Button
    private lateinit var falseBtn: Button
    private lateinit var nextBtn: ImageButton
    private lateinit var prevBtn: ImageButton
    private lateinit var questionTextView: TextView

    private val  questionList = listOf(
        Question(R.string.question_my_name, false),
        Question(R.string.question_baldur, true),
        Question(R.string.question_shadowheart, true),
        Question(R.string.question_raphael, false),
        Question(R.string.question_dostoevsky, false),
    )
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trueBtn = findViewById(R.id.true_button)
        falseBtn = findViewById(R.id.false_button)
        nextBtn = findViewById(R.id.next_button)
        prevBtn = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueBtn.setOnClickListener{
            checkAnswer(true)
        }
        falseBtn.setOnClickListener {
            checkAnswer(false)
        }
        nextBtn.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionList.size
            updateQuestion()
        }
        prevBtn.setOnClickListener {
            currentIndex = (currentIndex - 1) % questionList.size
            if (currentIndex == -1) currentIndex = questionList.size - 1
            updateQuestion()
        }
        questionTextView.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionList.size
            updateQuestion()
        }

        updateQuestion()
    }

    private fun updateQuestion() {
        questionTextView.setText(questionList[currentIndex].textResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        if (userAnswer == questionList[currentIndex].answer)
            Toast.makeText(this, R.string.correct_toast, Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show()
    }
}