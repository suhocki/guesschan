package com.example.guesschan

sealed class Screen {
    data class Game(
        val url: String,
        val letters: String,
    ): Screen()

    data class Correct(
        val next: Screen,
    ): Screen()

    data class Failure(
        val next: Screen,
        val answer: String,
    ): Screen()
}