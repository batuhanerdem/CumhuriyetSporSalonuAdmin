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
            binding.bottomNavigationView.isGone = when (navDestination.id) {
                R.id.addLessonFragment,
                R.id.studentListingByLessonFragment,
                R.id.addStudentFragment,
                R.id.lessonListingByStudent,
                R.id.editStudentProfileFragment,
                R.id.studentProfileFragment -> true

                else -> false
            }
        }
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}