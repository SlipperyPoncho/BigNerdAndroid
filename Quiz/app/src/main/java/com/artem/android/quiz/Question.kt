package com.artem.android.quiz

import androidx.annotation.StringRes

data class Question(@StringRes val textResId: Int, val answer: Boolean)