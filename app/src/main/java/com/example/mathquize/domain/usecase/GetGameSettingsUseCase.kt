package com.example.mathquize.domain.usecase

import com.example.mathquize.domain.entity.GameSettings
import com.example.mathquize.domain.entity.Level
import com.example.mathquize.domain.repository.MathQuizeRepository

class GetGameSettingsUseCase(private val mathQuizeRepository: MathQuizeRepository) {
    operator fun invoke(level: Level): GameSettings {
        return mathQuizeRepository.getGameSettings(level)
    }
}
