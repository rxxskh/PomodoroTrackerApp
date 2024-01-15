package com.rxs.pomodorotracker.domain.use_case

import com.rxs.pomodorotracker.common.DispatcherProvider
import com.rxs.pomodorotracker.data.model.PomodoroData
import com.rxs.pomodorotracker.domain.repository.DataRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetDataUseCase @Inject constructor(
    private val dataRepository: DataRepository,
    private val dispatcherProvider: DispatcherProvider
) {

    suspend fun invoke(): PomodoroData = withContext(dispatcherProvider.io) {
        dataRepository.get()
    }
}