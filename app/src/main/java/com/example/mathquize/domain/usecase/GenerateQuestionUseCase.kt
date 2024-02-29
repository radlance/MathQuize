package com.example.mathquize.domain.usecase

import com.example.mathquize.domain.entity.Question
import com.example.mathquize.domain.repository.MathQuizeRepository

class GenerateQuestionUseCase(private val mathQuizeRepository: MathQuizeRepository) {
    operator fun invoke(maxSumValue: Int): Question {
        return mathQuizeRepository.generateQuestion(maxSumValue, COUNT_OF_OPTIONS)
    }

    private companion object {
        private const val COUNT_OF_OPTIONS = 6
    }
}