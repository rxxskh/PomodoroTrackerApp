package com.rxs.pomodorotracker.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.rxs.pomodorotracker.R
import com.rxs.pomodorotracker.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomMenu()
    }

    private fun setupBottomMenu() {
        supportFragmentManager.findFragmentById(R.id.fragment_activity_main)?.let { fragment ->
            val navController = fragment.findNavController()
            binding.bottomNavigationActivityMain.setupWithNavController(navController)
        }
    }
}