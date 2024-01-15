package com.rxs.pomodorotracker.domain.repository

import com.rxs.pomodorotracker.data.model.PomodoroData

interface DataRepository {
    suspend fun save(data: PomodoroData)
    suspend fun get(): PomodoroData
}