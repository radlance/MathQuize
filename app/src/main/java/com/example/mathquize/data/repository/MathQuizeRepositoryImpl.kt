package com.example.mathquize.data.repository

import com.example.mathquize.domain.entity.GameSettings
import com.example.mathquize.domain.entity.Level
import com.example.mathquize.domain.entity.Question
import com.example.mathquize.domain.repository.MathQuizeRepository
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class MathQuizeRepositoryImpl : MathQuizeRepository {
    override fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question {
        val sum = Random.nextInt(MIN_SUM_VALUE, maxSumValue + 1)
        val visibleNumber = sum - Random.nextInt(MIN_VISIBLE_VALUE, sum)
        val options = HashSet<Int>()
        val rightAnswer = sum - visibleNumber
        options.add(rightAnswer)
        val from = max(rightAnswer - countOfOptions, MIN_VISIBLE_VALUE)
        val to = min(maxSumValue - 1, rightAnswer + countOfOptions)
        while (options.size < countOfOptions) {
            options.add(Random.nextInt(from, to))
        }
        return Question(sum = sum, visibleNumber = visibleNumber, options = options.toList())
    }

    override fun getGameSettings(level: Level): GameSettings {
        return when (level) {
            Level.TEST -> {
                GameSettings(
                    10,
                    3,
                    50,
                    8
                )
            }

            Level.EASY -> {
                GameSettings(
                    10,
                    10,
                    70,
                    60
                )
            }

            Level.MEDIUM -> {
                GameSettings(
                    20,
                    20,
                    80,
                    40
                )
            }

            Level.HARD -> {
                GameSettings(
                    30,
                    30,
                    90,
                    35
                )
            }
        }
    }

    private companion object {
        private const val MIN_SUM_VALUE = 2
        private const val MIN_VISIBLE_VALUE = 1
    }
}