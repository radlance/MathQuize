package com.example.mathquize.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    retryGame()
                }
            }
        )
        binding.buttonTryAgain.setOnClickListener {
            retryGame()
        }
    }

    private fun retryGame() {
        requireActivity()
            .supportFragmentManager
            .popBackStack(GameFragment.FRAGMENT_NAME, FragmentManager.POP_BACK_STACK_INCLUSIVE)
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