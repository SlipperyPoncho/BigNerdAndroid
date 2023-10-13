package com.artem.android.quiz

import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {
    private val  questionList = listOf(
        Question(R.string.question_my_name, false),
        Question(R.string.question_baldur, true),
        Question(R.string.question_shadowheart, true),
        Question(R.string.question_raphael, false),
        Question(R.string.question_dostoevsky, false),
    )

    private var currentIndex = 0
    var allAnswers: Int = 0
    var correctAnswers: Int = 0

    val currentQuestionAnswer: Boolean
        get() = questionList[currentIndex].answer
    var currentQuestionAnswered: Boolean
        get() = questionList[currentIndex].isAnswered
        set(value) { questionList[currentIndex].isAnswered = value }
    var currentQuestionCheated: Boolean
        get() = questionList[currentIndex].isCheated
        set(value) { questionList[currentIndex].isCheated = value }
    val currentQuestionText: Int
        get() = questionList[currentIndex].textResId

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionList.size
    }

    fun moveToPrev() {
        currentIndex = (currentIndex - 1) % questionList.size
        if (currentIndex == -1) currentIndex = questionList.size - 1
    }

    fun getQuestionsSize(): Int { return questionList.size }
}