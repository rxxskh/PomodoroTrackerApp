package com.rxs.pomodorotracker.presentation.timer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.rxs.pomodorotracker.R
import com.rxs.pomodorotracker.common.ActionType
import com.rxs.pomodorotracker.common.concatPomodoroText
import com.rxs.pomodorotracker.common.toTimeText
import com.rxs.pomodorotracker.databinding.FragmentTimerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TimerFragment : Fragment() {

    private lateinit var binding: FragmentTimerBinding
    private val args: TimerFragmentArgs by navArgs()
    private val viewModel: TimerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTimerBinding.inflate(layoutInflater)
        setupView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setCurrentTask(task = args.currentTask)
        startObserve()
    }

    override fun onPause() {
        super.onPause()
        viewModel.updateTask()
    }

    private fun startObserve() {
        viewModel.remainingTime.observe(viewLifecycleOwner) {
            binding.apply {
                tvFragmentTimerRingValue.text = it.toTimeText()
            }
        }
        viewModel.currentTask.observe(viewLifecycleOwner) {
            Log.d("FATAL", "OBSERVE CURRENT TASK")
            binding.apply {
                tvFragmentTimerTitle.text = it.name
                tvFragmentTimerTomatoCount.text =
                    concatPomodoroText(complete = it.tomatoesComplete, goal = it.tomatoesGoal)

                if (it.stopType == ActionType.WORK) {
                    tvFragmentTimerFullTitleName.text = "Работа"
                    ivFragmentTimerFullTitleIcon.setImageResource(R.drawable.shape_work_ring)
                    pbFragmentTimerRing.progressDrawable =
                        ResourcesCompat.getDrawable(resources, R.drawable.progress_bar_work, null)

                } else {
                    tvFragmentTimerFullTitleName.text = "Отдых"
                    ivFragmentTimerFullTitleIcon.setImageResource(R.drawable.shape_relax_ring)
                    pbFragmentTimerRing.progressDrawable =
                        ResourcesCompat.getDrawable(resources, R.drawable.progress_bar_relax, null)
                }
            }
        }
        viewModel.progressPercent.observe(viewLifecycleOwner) {
            Log.d("FATAL", "OBSERVE PROGRESS $it")
            binding.pbFragmentTimerRing.progress = it
        }
    }

    private fun setupView() {
        binding.apply {
            ivFragmentTimerBack.setOnClickListener {
                Navigation.createNavigateOnClickListener(
                    R.id.action_timerFragment_to_taskListFragment
                ).onClick(it)
            }
            ivFragmentTimerDelete.setOnClickListener {
                viewModel.deleteTask()
                Navigation.createNavigateOnClickListener(
                    R.id.action_timerFragment_to_taskListFragment
                ).onClick(it)
            }
            llFragmentTimerPlay.setOnClickListener {
                viewModel.startTimer()
                it.visibility = View.GONE
            }
            llFragmentTimerPause.setOnClickListener {
                viewModel.stopTimer()
                llFragmentTimerPlay.visibility = View.VISIBLE
            }
            llFragmentTimerSkip.setOnClickListener {
                viewModel.nextTomato()
            }
            tvFragmentTimerReset.setOnClickListener {
                viewModel.resetTomatoes()
            }
        }
    }
}