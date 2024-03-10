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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mathquize.databinding.FragmentGameBinding
import com.example.mathquize.domain.entity.GameResult


class GameFragment : Fragment() {
    private val args by navArgs<GameFragmentArgs>()
    private var _binding: FragmentGameBinding? = null
    private val vm: GameFragmentViewModel by lazy {
        ViewModelProvider(
            this,
            GameFragmentViewModelFactory(requireActivity().applicationContext, args.level)
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
        findNavController().navigate(
            GameFragmentDirections.actionGameFragmentToGameFinishedFragment(gameResult)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}