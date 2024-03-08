package com.example.mathquize.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mathquize.R
import com.example.mathquize.databinding.FragmentGameBinding
import com.example.mathquize.domain.entity.GameResult
import com.example.mathquize.domain.entity.GameSettings
import com.example.mathquize.domain.entity.Level


class GameFragment : Fragment() {
    private lateinit var level: Level

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvAnswer1.setOnClickListener {
            launchGameFinishedFragment(
                GameResult(
                    true,
                    1,
                    1,
                    GameSettings(
                        1,
                        1,
                        1,
                        1
                    )
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchGameFinishedFragment(gameResult: GameResult) {
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.main_container, GameFinishedFragment.newInstance(gameResult))
            .commit()
    }

    @Suppress("DEPRECATION")
    private fun parseArgs() {
        requireArguments().getParcelable<Level>(KEY_LEVEL)?.let {
            level = it
        }
    }

    companion object {
        private const val KEY_LEVEL = "level"
        const val FRAGMENT_NAME = "previous_fragment"
        fun newInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LEVEL, level)
                }
            }
        }
    }
}