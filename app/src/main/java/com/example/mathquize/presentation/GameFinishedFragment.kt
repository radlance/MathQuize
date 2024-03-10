package com.example.mathquize.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mathquize.R
import com.example.mathquize.databinding.FragmentGameFinishedBinding

class GameFinishedFragment : Fragment() {
    private val args by navArgs<GameFinishedFragmentArgs>()
    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

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
        binding.buttonTryAgain.setOnClickListener { retryGame() }
        setupGameResult()
    }


    private fun retryGame() {
        findNavController().popBackStack()
    }

    private fun setupGameResult() {
        val smile = if (args.gameResult.winner) {
            R.drawable.ic_smile
        } else {
            R.drawable.ic_sad
        }

        val percent =
            (args.gameResult.countOfRightAnswers / args.gameResult.countOfQuestions.toDouble() * 100).toInt()
        with(binding) {
            ivSmile.setImageResource(smile)
            tvMinAnswers.text = formattedString(
                R.string.min_answers,
                args.gameResult.gameSettings.minCountOfRightAnswers
            )

            tvScore.text = formattedString(R.string.score, args.gameResult.countOfRightAnswers)
            tvMinPercent.text = formattedString(
                R.string.min_percent,
                args.gameResult.gameSettings.minPercentageOfRightAnswers
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
}