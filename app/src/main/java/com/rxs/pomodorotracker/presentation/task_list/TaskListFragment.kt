package com.rxs.pomodorotracker.presentation.task_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.rxs.pomodorotracker.R
import com.rxs.pomodorotracker.common.ActionType
import com.rxs.pomodorotracker.common.toTimeText
import com.rxs.pomodorotracker.databinding.FragmentTaskListBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TaskListFragment : Fragment() {

    private lateinit var binding: FragmentTaskListBinding
    private val viewModel: TaskListViewModel by viewModels()

    @Inject
    lateinit var taskListAdapter: TaskListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskListBinding.inflate(layoutInflater)
        setupView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startObserve()
    }

    private fun startObserve() {
        viewModel.pomodoroData.observe(viewLifecycleOwner) {
            if (it.recentTask != null) {
                binding.apply {
                    it.recentTask!!.apply {
                        tvFragmentTaskListRecentTitle.text = name

                        if (stopType == ActionType.WORK) {
                            ivFragmentTaskListTypeRing.setImageResource(R.drawable.shape_work_ring)
                            tvFragmentTaskListRecentTypeRelax.visibility = View.GONE
                            tvFragmentTaskListRecentTypeWork.visibility = View.VISIBLE
                        } else {
                            ivFragmentTaskListTypeRing.setImageResource(R.drawable.shape_relax_ring)
                            tvFragmentTaskListRecentTypeWork.visibility = View.GONE
                            tvFragmentTaskListRecentTypeRelax.visibility = View.VISIBLE
                        }

                        val remainingTime =
                            (if (stopType == ActionType.WORK) workTimeInMinutes else relaxTimeInMinutes) * 60L - passedTimeInSeconds
                        tvFragmentTaskListRecentTimer.text = remainingTime.toTimeText()

                        cvFragmentTaskListRecentTask.setOnClickListener {
                            Navigation.createNavigateOnClickListener(
                                TaskListFragmentDirections.actionTaskListFragmentToTimerFragment(
                                    currentTask = this
                                )
                            ).onClick(it)
                        }
                    }

                    cvFragmentTaskListRecentTask.visibility = View.VISIBLE
                }
            } else {
                binding.cvFragmentTaskListRecentTask.visibility = View.GONE
            }
            taskListAdapter.submitData(it.taskList)
        }
    }

    private fun setupView() {
        binding.apply {
            rvFragmentTaskList.apply {
                layoutManager =
                    LinearLayoutManager(
                        this@TaskListFragment.context,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                adapter = taskListAdapter
            }
        }
    }
}