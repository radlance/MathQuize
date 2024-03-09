package com.example.mathquize.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mathquize.R
import com.example.mathquize.databinding.FragmentGameBinding
import com.example.mathquize.domain.entity.GameResult
import com.example.mathquize.domain.entity.Level


class GameFragment : Fragment() {
    private lateinit var level: Level

    private var _binding: FragmentGameBinding? = null
    private val vm: GameFragmentViewModel by lazy {
        ViewModelProvider(
            this,
            GameFragmentViewModelFactory(requireActivity().applicationContext, level)
        )[GameFragmentViewModel::class.java]
    }

    private val tvAnswers by lazy {
        mutableListOf<TextView>().apply {
            add(binding.tvAnswer1)
            add(binding.tvAnswer2)
            add(binding.tvAnswer3)
            add(binding.tvAnswer4)
            add(binding.tvAnswer5)
            add(binding.tvAnswer6)
        }
    }
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
        observeLiveData()

    }

    private fun observeLiveData() {
        observeTimer()
        observeQuestion()
        observeProgress()
        finishGame()
    }

    private fun finishGame() {
        vm.gameResult.observe(viewLifecycleOwner) {
            launchGameFinishedFragment(it)
        }
    }

    private fun observeTimer() {
        vm.time.observe(viewLifecycleOwner) {
            binding.tvTimer.text = it
        }
    }

    private fun observeProgress() {
        vm.progressAnswer.observe(viewLifecycleOwner) {
            binding.tvRightPercent.text = it
        }

        vm.enoughCountOfRightAnswers.observe(viewLifecycleOwner) {
            val color = getColorByState(it)
            binding.tvRightPercent.setTextColor(
                ContextCompat.getColor(
                    requireActivity().applicationContext,
                    color
                )
            )
        }

        vm.percentOfRightAnswers.observe(viewLifecycleOwner) {
            binding.pbRightAnswers.setProgress(it, true)
        }

        vm.enoughPercent.observe(viewLifecycleOwner) {
            val color = getColorByState(it)
            binding.pbRightAnswers.progressTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireActivity().applicationContext,
                    color
                )
            )
        }

        vm.minPercent.observe(viewLifecycleOwner) {
            binding.pbRightAnswers.secondaryProgress = it
        }
    }

    private fun observeQuestion() {
        vm.question.observe(viewLifecycleOwner) {
            with(binding) {
                tvSum.text = it.sum.toString()
                tvVisible.text = it.visibleNumber.toString()
            }

            for ((textView, answer) in tvAnswers.zip(it.options)) {
                textView.text = answer.toString()
                textView.setOnClickListener {
                    vm.chooseAnswer(textView.text.toString().toInt())
                }
            }
        }
    }

    private fun getColorByState(state: Boolean): Int {
        return if (state) {
            android.R.color.holo_green_light
        } else {
            android.R.color.holo_red_light
        }
    }

    private fun launchGameFinishedFragment(gameResult: GameResult) {
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.main_container, GameFinishedFragment.newInstance(gameResult))
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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