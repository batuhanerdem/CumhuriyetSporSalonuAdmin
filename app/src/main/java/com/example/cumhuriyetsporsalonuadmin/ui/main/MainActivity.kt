package com.example.cumhuriyetsporsalonuadmin.ui.main

import androidx.core.view.isGone
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.cumhuriyetsporsalonuadmin.R
import com.example.cumhuriyetsporsalonuadmin.databinding.ActivityMainBinding
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<MainActionBus, MainViewModel, ActivityMainBinding>(
    ActivityMainBinding::inflate, MainViewModel::class.java
) {
    override fun init() {

        setNavListeners()
    }

    override suspend fun onAction(action: MainActionBus) {

    }

    private fun setNavListeners() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, navDestination, _ ->
            binding.bottomNavigationView.isGone = navDestination.id == R.id.addLessonFragment
        }
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}