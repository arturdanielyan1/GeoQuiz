package com.bignerdranch.android.geoquiz2

data class Question(val resource: Int, val isTrue: Boolean) {
    companion object {
        @JvmStatic
        val questions = arrayListOf(
            Question(R.string.question1, true),
            Question(R.string.question2, true),
            Question(R.string.question3, false),
            Question(R.string.question4, false),
            Question(R.string.question5, true),
            Question(R.string.question6, true)
        )
    }

    var isAnswered: Boolean = false
    var hasCheated: Boolean = false
}