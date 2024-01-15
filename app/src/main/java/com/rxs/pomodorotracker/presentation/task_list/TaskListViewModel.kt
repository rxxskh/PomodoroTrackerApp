package com.rxs.pomodorotracker.presentation.task_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rxs.pomodorotracker.data.model.PomodoroData
import com.rxs.pomodorotracker.domain.use_case.GetDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val getDataUseCase: GetDataUseCase
) : ViewModel() {

    private val _pomodoroData = MutableLiveData<PomodoroData>()
    val pomodoroData: LiveData<PomodoroData> = _pomodoroData

    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            _pomodoroData.postValue(getDataUseCase.invoke())
        }
    }
}