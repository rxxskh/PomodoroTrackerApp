package com.rxs.pomodorotracker.data.model

import android.os.Parcelable
import com.rxs.pomodorotracker.common.ActionType
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    var name: String = "Без названия",
    var workTimeInMinutes: Int = 25,
    var relaxTimeInMinutes: Int = 7,
    var tomatoesComplete: Int = 0,
    var tomatoesGoal: Int = 8,
    var passedTimeInSeconds: Long = 0,
    var stopType: ActionType = ActionType.WORK
) : Parcelable
