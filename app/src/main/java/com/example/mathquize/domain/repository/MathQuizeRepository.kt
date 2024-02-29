package com.example.mathquize.domain.repository

import com.example.mathquize.domain.entity.GameSettings
import com.example.mathquize.domain.entity.Level
import com.example.mathquize.domain.entity.Question

interface MathQuizeRepository {
    fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question
    fun getGameSettings(level: Level): GameSettings
}