package com.example.mathquize.presentation

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import com.example.mathquize.domain.entity.GameResult
import com.example.mathquize.domain.entity.GameSettings
import com.example.mathquize.domain.entity.Level
import com.example.mathquize.domain.entity.Question
import com.example.mathquize.domain.usecase.GenerateQuestionUseCase
import com.example.mathquize.domain.usecase.GetGameSettingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameFragmentViewModel(
    private val generateQuestionUseCase: GenerateQuestionUseCase,
    private val getGameSettingsUseCase: GetGameSettingsUseCase,
    private val progress: String,
    private val level: Level,
) : ViewModel() {

    private lateinit var gameSettings: GameSettings
    private var countOfRightAnswers = 0
    private var countOfQuestions = 0
    private var timer: CountDownTimer? = null

    private var _time = MutableStateFlow("60")
    val time: StateFlow<String> = _time.asStateFlow()

    private val _question = MutableStateFlow(Question(0, 0, emptyList()))
    val question: StateFlow<Question> = _question.asStateFlow()

    private val _percentOfRightAnswers = MutableStateFlow(0)
    val percentOfRightAnswers: StateFlow<Int> = _percentOfRightAnswers.asStateFlow()

    private val _progressAnswers = MutableStateFlow("0")
    val progressAnswer: StateFlow<String> = _progressAnswers.asStateFlow()

    private val _enoughCountOfRightAnswers = MutableStateFlow(false)
    val enoughCountOfRightAnswers: StateFlow<Boolean> = _enoughCountOfRightAnswers.asStateFlow()

    private val _enoughCountOfPercent = MutableStateFlow(false)
    val enoughPercent: StateFlow<Boolean> = _enoughCountOfPercent.asStateFlow()

    private val _minPercent = MutableStateFlow(0)
    val minPercent: StateFlow<Int> = _minPercent.asStateFlow()

    private val _gameResult: MutableStateFlow<GameResult?> = MutableStateFlow(null)

    val gameResult: StateFlow<GameResult?> = _gameResult.asStateFlow()

    init {
        getGameSettings()
        startTimer()
        generateQuestion()
        updateProgress()
    }


    private fun getGameSettings() {
        gameSettings = getGameSettingsUseCase(level)
        _minPercent.value = gameSettings.minPercentageOfRightAnswers
    }

    private fun startTimer() {
        timer = object :
            CountDownTimer(gameSettings.gameTimeInSeconds * MILLIS_IN_SECONDS, MILLIS_IN_SECONDS) {
            override fun onTick(millisUntilFinished: Long) {
                _time.value = formattedTime(millisUntilFinished)
            }

            override fun onFinish() {
                finishGame()
            }
        }
        timer?.start()
    }

    fun chooseAnswer(number: Int) {
        checkAnswer(number)
        generateQuestion()
        updateProgress()
    }

    private fun checkAnswer(number: Int) {
        if (number == _question.value.rightAnswer) {
            countOfRightAnswers++
        }
        countOfQuestions++
    }

    private fun generateQuestion() {
        _question.value = generateQuestionUseCase(gameSettings.maxSumValue)
    }

    private fun updateProgress() {
        val percent = calculatePercentOfRightAnswers()
        _percentOfRightAnswers.value = percent
        _progressAnswers.value =
            String.format(progress, countOfRightAnswers, gameSettings.minCountOfRightAnswers)

        _enoughCountOfRightAnswers.value =
            countOfRightAnswers >= gameSettings.minCountOfRightAnswers

        _enoughCountOfPercent.value = percent >= gameSettings.minPercentageOfRightAnswers
    }

    private fun calculatePercentOfRightAnswers(): Int {
        return ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
    }

    private fun finishGame() {
        _gameResult.value = GameResult(
            winner = _enoughCountOfRightAnswers.value && _enoughCountOfPercent.value,
            countOfRightAnswers = countOfRightAnswers,
            countOfQuestions = countOfQuestions,
            gameSettings = gameSettings
        )
    }

    override fun onCleared() {
        super.onCleared()
        timer = null
    }

    private fun formattedTime(millisUntilFinished: Long): String {
        val seconds = millisUntilFinished / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTES)
        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    companion object {
        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECONDS_IN_MINUTES = 60
    }
}