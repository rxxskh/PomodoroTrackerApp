package com.rxs.pomodorotracker.presentation.new_task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rxs.pomodorotracker.data.model.Task
import com.rxs.pomodorotracker.domain.use_case.CreateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewTaskViewModel @Inject constructor(
    private val createTaskUseCase: CreateTaskUseCase
) : ViewModel() {

    private val _workTimeInMinutes = MutableLiveData<Int>()
    val workTimeInMinutes: LiveData<Int> = _workTimeInMinutes

    private val _relaxTimeInMinutes = MutableLiveData<Int>()
    val relaxTimeInMinutes: LiveData<Int> = _relaxTimeInMinutes

    private val _tomatoesGoal = MutableLiveData<Int>()
    val tomatoesGoal: LiveData<Int> = _tomatoesGoal

    init {
        _workTimeInMinutes.value = 25
        _relaxTimeInMinutes.value = 7
        _tomatoesGoal.value = 8
    }

    fun increaseWorkTime() {
        if (_workTimeInMinutes.value!! < 60) _workTimeInMinutes.value = _workTimeInMinutes.value!! + 1
    }

    fun increaseRelaxTime() {
        if (_relaxTimeInMinutes.value!! < 60) _relaxTimeInMinutes.value = _relaxTimeInMinutes.value!! + 1
    }

    fun increaseTomatoesGoal() {
        if (_tomatoesGoal.value!! < 30) _tomatoesGoal.value = _tomatoesGoal.value!! + 1
    }

    fun decreaseWorkTime() {
        if (_workTimeInMinutes.value!! > 5) _workTimeInMinutes.value = _workTimeInMinutes.value!! - 1
    }

    fun decreaseRelaxTime() {
        if (_relaxTimeInMinutes.value!! > 5) _relaxTimeInMinutes.value = _relaxTimeInMinutes.value!! - 1
    }

    fun decreaseTomatoesGoal() {
        if (_tomatoesGoal.value!! > 2) _tomatoesGoal.value = _tomatoesGoal.value!! - 1
    }

    fun createTask(name: String) {
        viewModelScope.launch {
            createTaskUseCase.invoke(
                Task(
                    name = name,
                    workTimeInMinutes = _workTimeInMinutes.value!!,
                    relaxTimeInMinutes = _relaxTimeInMinutes.value!!,
                    tomatoesGoal = _tomatoesGoal.value!!
                )
            )
        }
    }
}