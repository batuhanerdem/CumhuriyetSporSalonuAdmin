package com.example.cumhuriyetsporsalonuadmin.ui.main.home.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cumhuriyetsporsalonuadmin.ui.main.home.lesson_request.LessonRequestFragment
import com.example.cumhuriyetsporsalonuadmin.ui.main.home.verify_request.VerifyRequestFragment

class FragmentAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {


    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> VerifyRequestFragment()
            1 -> LessonRequestFragment()
            else -> VerifyRequestFragment()
        }

    }

    override fun getItemCount(): Int {
        return 2
    }
}