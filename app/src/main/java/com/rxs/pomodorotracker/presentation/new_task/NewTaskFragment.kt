package com.rxs.pomodorotracker.presentation.new_task

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.rxs.pomodorotracker.R
import com.rxs.pomodorotracker.common.toStringWithMin
import com.rxs.pomodorotracker.databinding.FragmentNewTaskBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewTaskFragment : Fragment() {

    private lateinit var binding: FragmentNewTaskBinding
    private val viewModel: NewTaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewTaskBinding.inflate(layoutInflater)
        setupView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startObserve()
    }

    private fun startObserve() {
        viewModel.workTimeInMinutes.observe(viewLifecycleOwner) {
            binding.tvFragmentNewTaskWorkTime.text = it.toStringWithMin()
        }
        viewModel.relaxTimeInMinutes.observe(viewLifecycleOwner) {
            binding.tvFragmentNewTaskRelaxTime.text = it.toStringWithMin()
        }
        viewModel.tomatoesGoal.observe(viewLifecycleOwner) {
            binding.tvFragmentNewTaskTomato.text = it.toString()
        }
    }

    private fun setupView() {
        binding.apply {
            btnFragmentNewTaskCreate.setOnClickListener {
                val name = etFragmentNewTaskName.text.toString()
                if (name.isNotBlank() && name != "Без названия?") {
                    viewModel.createTask(name = etFragmentNewTaskName.text.toString())

                    Navigation.createNavigateOnClickListener(
                        R.id.action_newTaskFragment_to_taskListFragment
                    ).onClick(it)
                } else {
                    etFragmentNewTaskName.text =
                        Editable.Factory.getInstance().newEditable("Без названия?")
                }
            }

            ivFragmentNewTaskWorkTimePlus.setOnClickListener { viewModel.increaseWorkTime() }
            ivFragmentNewTaskWorkTimeMinus.setOnClickListener { viewModel.decreaseWorkTime() }
            ivFragmentNewTaskRelaxTimePlus.setOnClickListener { viewModel.increaseRelaxTime() }
            ivFragmentNewTaskRelaxTimeMinus.setOnClickListener { viewModel.decreaseRelaxTime() }
            ivFragmentNewTaskTomatoPlus.setOnClickListener { viewModel.increaseTomatoesGoal() }
            ivFragmentNewTaskTomatoMinus.setOnClickListener { viewModel.decreaseTomatoesGoal() }
        }
    }
}