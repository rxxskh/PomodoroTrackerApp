package com.rxs.pomodorotracker.domain.use_case

import android.util.Log
import com.rxs.pomodorotracker.common.DispatcherProvider
import com.rxs.pomodorotracker.data.model.Task
import com.rxs.pomodorotracker.domain.repository.DataRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val dataRepository: DataRepository,
    private val dispatcherProvider: DispatcherProvider
) {

    suspend fun invoke(oldTask: Task, updatedTask: Task, recentTask: Task) {
        withContext(dispatcherProvider.io) {
            val sharedPomodoroData = dataRepository.get()
            sharedPomodoroData.taskList.indexOf(oldTask).let { index ->
                if (index != -1) {
                    sharedPomodoroData.taskList[index] = updatedTask
                }
            }
            sharedPomodoroData.recentTask = recentTask
            dataRepository.save(data = sharedPomodoroData)
        }
    }
}