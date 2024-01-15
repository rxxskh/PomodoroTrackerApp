package com.rxs.pomodorotracker.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.google.gson.Gson
import com.rxs.pomodorotracker.common.PREFS_KEY
import com.rxs.pomodorotracker.data.repository.SharedPreferencesRepository
import com.rxs.pomodorotracker.domain.repository.DataRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideDataRepository(sharedPreferences: SharedPreferences, gson: Gson): DataRepository =
        SharedPreferencesRepository(sharedPreferences = sharedPreferences, gson = gson)
}