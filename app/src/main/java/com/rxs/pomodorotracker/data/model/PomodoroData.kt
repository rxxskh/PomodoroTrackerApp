package com.rxs.pomodorotracker.data.model

data class PomodoroData(
    var taskList: MutableList<Task> = mutableListOf(),
    var recentTask: Task? = null
)
