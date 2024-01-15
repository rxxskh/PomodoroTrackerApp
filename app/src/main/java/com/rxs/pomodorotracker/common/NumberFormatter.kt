package com.rxs.pomodorotracker.common

fun Int.toStringWithZeros(): String = "$this:00"

fun Int.toStringWithMin(): String = "$this мин"

fun Long.toTimeText(): String = String.format("%02d:%02d", this / 60, this % 60)

fun concatPomodoroText(complete: Int, goal: Int): String = "$complete из $goal"