package com.example.mathquize.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mathquize.R
import com.example.mathquize.databinding.FragmentGameFinishedBinding
import com.example.mathquize.domain.entity.GameResult

class GameFinishedFragment : Fragment() {
    private var _binding: FragmentGameFinishedBinding? = null
    private lateinit var gameResult: GameResult
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addCallback()
        binding.buttonTryAgain.setOnClickListener { retryGame() }
        setupGameResult()
    }

    private fun addCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    retryGame()
                }
            }
        )
    }

    private fun retryGame() {
        requireActivity()
            .supportFragmentManager
            .popBackStack(GameFragment.FRAGMENT_NAME, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    private fun setupGameResult() {
        val smile = if (gameResult.winner) {
            R.drawable.ic_smile
        } else {
            R.drawable.ic_sad
        }

        val percent =
            (gameResult.countOfRightAnswers / gameResult.countOfQuestions.toDouble() * 100).toInt()
        with(binding) {
            ivSmile.setImageResource(smile)
            tvMinAnswers.text = formattedString(
                R.string.min_answers,
                gameResult.gameSettings.minCountOfRightAnswers
            )

            tvScore.text = formattedString(R.string.score, gameResult.countOfRightAnswers)
            tvMinPercent.text = formattedString(
                R.string.min_percent,
                gameResult.gameSettings.minPercentageOfRightAnswers
            )

            tvPercent.text = formattedString(R.string.percent_of_right_answers, percent)
        }
    }

    private fun formattedString(stringResource: Int, value: Int): String {
        return String.format(getString(stringResource), value)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Suppress("DEPRECATION")
    private fun parseArgs() {
        requireArguments().getParcelable<GameResult>(KEY_RESULT)?.let {
            gameResult = it
        }
    }

    companion object {
        private const val KEY_RESULT = "result"
        fun newInstance(gameResult: GameResult): GameFinishedFragment {
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_RESULT, gameResult)
                }
            }
        }
    }
}