package com.rxs.pomodorotracker.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.rxs.pomodorotracker.common.DATA_KEY
import com.rxs.pomodorotracker.data.model.PomodoroData
import com.rxs.pomodorotracker.domain.repository.DataRepository
import javax.inject.Inject

class SharedPreferencesRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : DataRepository {
    override suspend fun save(data: PomodoroData) {
        with(sharedPreferences.edit()) {
            val json: String = gson.toJson(data)
            putString(DATA_KEY, json)
            apply()
        }
    }

    override suspend fun get(): PomodoroData {
        val json: String? = sharedPreferences.getString(DATA_KEY, null)
        return if (json != null) {
            gson.fromJson(json, PomodoroData::class.java)
        } else {
            PomodoroData()
        }
    }
}