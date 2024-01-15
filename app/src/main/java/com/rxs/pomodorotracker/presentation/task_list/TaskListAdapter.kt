package com.rxs.pomodorotracker.presentation.task_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.rxs.pomodorotracker.R
import com.rxs.pomodorotracker.common.concatPomodoroText
import com.rxs.pomodorotracker.common.toStringWithZeros
import com.rxs.pomodorotracker.data.model.Task
import com.rxs.pomodorotracker.databinding.ItemTaskBinding
import javax.inject.Inject

class TaskListAdapter @Inject constructor() :
    RecyclerView.Adapter<TaskListAdapter.TaskListViewHolder>() {

    private lateinit var binding: ItemTaskBinding
    private var taskList = emptyList<Task>()


    inner class TaskListViewHolder : RecyclerView.ViewHolder(binding.root) {

        fun setData(task: Task) = binding.apply {
            tvItemTaskTitle.text = task.name
            tvItemTaskWorkTime.text = task.workTimeInMinutes.toStringWithZeros()
            tvItemTaskRelaxTime.text = task.relaxTimeInMinutes.toStringWithZeros()
            tvItemTaskCompletedText.text =
                concatPomodoroText(complete = task.tomatoesComplete, goal = task.tomatoesGoal)
            clItemTask.setOnClickListener {
                Navigation.createNavigateOnClickListener(
                    TaskListFragmentDirections.actionTaskListFragmentToTimerFragment(currentTask = task)
                ).onClick(it)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListViewHolder {
        binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskListViewHolder()
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        holder.setData(task = taskList[position])
    }

    fun submitData(data: List<Task>) {
        taskList = data
        notifyDataSetChanged()
    }
}