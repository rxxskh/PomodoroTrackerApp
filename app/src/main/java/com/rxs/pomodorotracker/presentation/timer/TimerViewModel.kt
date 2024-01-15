package com.rxs.pomodorotracker.presentation.timer

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rxs.pomodorotracker.common.ActionType
import com.rxs.pomodorotracker.data.model.Task
import com.rxs.pomodorotracker.domain.use_case.DeleteTaskUseCase
import com.rxs.pomodorotracker.domain.use_case.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
) : ViewModel() {

    private lateinit var countDownTimer: CountDownTimer
    private var goalTime: Long = 0
    private var isTimerActive: Boolean = false

    private lateinit var lastSavedTask: Task

    private val _remainingTime = MutableLiveData<Long>()
    val remainingTime: LiveData<Long> = _remainingTime

    private val _progressPercent = MutableLiveData<Int>()
    val progressPercent: LiveData<Int> = _progressPercent

    private val _currentTask = MutableLiveData<Task>()
    val currentTask: LiveData<Task> = _currentTask

    fun setCurrentTask(task: Task) {
        _currentTask.value = task.copy()
        updateLastSavedTask()
        setupData()
        updateTask()
    }

    fun startTimer() {
        recreateCountDownTimer()
        countDownTimer.start()
        isTimerActive = true
    }

    fun stopTimer() {
        isTimerActive = false
        countDownTimer.cancel()
    }

    fun nextTomato() {
        _currentTask.value!!.apply {
            tomatoesComplete =
                if (_currentTask.value!!.stopType == ActionType.WORK) _currentTask.value!!.tomatoesComplete + 1 else _currentTask.value!!.tomatoesComplete
            stopType =
                if (_currentTask.value!!.stopType == ActionType.WORK) ActionType.RELAX else ActionType.WORK
            passedTimeInSeconds = 0
        }
        updateTask()
        updateCurrentTaskLiveData()
        refreshProgressBar()
        setupData()
        if (isTimerActive) {
            countDownTimer.cancel()
            startTimer()
        }
    }

    fun deleteTask() {
        viewModelScope.launch {
            deleteTaskUseCase.invoke(task = _currentTask.value!!)
        }
    }

    fun resetTomatoes() {
        _currentTask.value!!.apply {
            tomatoesComplete = 0
            stopType = ActionType.WORK
            passedTimeInSeconds = 0
        }
        updateTask()
        updateCurrentTaskLiveData()
        refreshProgressBar()
        setupData()
        if (isTimerActive) countDownTimer.cancel()
    }

    fun updateTask() {
        viewModelScope.launch {
            updateTaskUseCase.invoke(
                oldTask = lastSavedTask,
                updatedTask = _currentTask.value!!,
                recentTask = _currentTask.value!!
            )
            updateLastSavedTask()
        }
    }

    private fun updateCurrentTaskLiveData() {
        _currentTask.value = _currentTask.value!!
    }

    private fun updateLastSavedTask() {
        lastSavedTask = _currentTask.value!!.copy()
    }

    private fun recreateCountDownTimer() {
        countDownTimer = object : CountDownTimer(_remainingTime.value!! * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val leftTimeInSeconds = millisUntilFinished / 1000
                _remainingTime.value = leftTimeInSeconds
                _progressPercent.value =
                    ((leftTimeInSeconds.toFloat() / goalTime.toFloat()) * 100).toInt()

                _currentTask.value!!.passedTimeInSeconds = goalTime - _remainingTime.value!!
            }

            override fun onFinish() {
                nextTomato()
            }
        }
    }

    private fun refreshProgressBar() {
        _progressPercent.value = 0
    }

    private fun setupData() {
        val stopType = _currentTask.value!!.stopType
        val workTimeInMinutes = _currentTask.value!!.workTimeInMinutes
        val relaxTimeInMinutes = _currentTask.value!!.relaxTimeInMinutes
        val passedTimeInSeconds = _currentTask.value!!.passedTimeInSeconds

        _remainingTime.value =
            (if (stopType == ActionType.WORK) workTimeInMinutes else relaxTimeInMinutes) * 60L - passedTimeInSeconds
        goalTime =
            (if (stopType == ActionType.WORK) workTimeInMinutes else relaxTimeInMinutes) * 60L
        _progressPercent.value =
            ((_remainingTime.value!!.toFloat() / goalTime.toFloat()) * 100).toInt()
    }
}