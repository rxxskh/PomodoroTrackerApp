package com.rxs.pomodorotracker.common

import com.rxs.pomodorotracker.presentation.task_list.TaskListAdapter
import javax.inject.Inject

class AdapterProvider @Inject constructor() {
    fun getTaskListAdapter(): TaskListAdapter = TaskListAdapter()
}