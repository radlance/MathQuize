package com.example.mathquize.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mathquize.R
import com.example.mathquize.data.repository.MathQuizeRepositoryImpl
import com.example.mathquize.domain.entity.Level
import com.example.mathquize.domain.usecase.GenerateQuestionUseCase
import com.example.mathquize.domain.usecase.GetGameSettingsUseCase

class GameFragmentViewModelFactory(context: Context, private val level: Level) : ViewModelProvider.Factory {
    private val mathQuizeRepositoryImpl = MathQuizeRepositoryImpl()

    private val generateQuestionUseCase = GenerateQuestionUseCase(mathQuizeRepositoryImpl)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(mathQuizeRepositoryImpl)
    private val progressAnswer = context.resources.getString(R.string.right_answers)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GameFragmentViewModel::class.java)) {
            return GameFragmentViewModel(
                generateQuestionUseCase,
                getGameSettingsUseCase,
                progressAnswer,
                level
            ) as T
        }
        throw RuntimeException("Unknown view model class $modelClass")
    }
}