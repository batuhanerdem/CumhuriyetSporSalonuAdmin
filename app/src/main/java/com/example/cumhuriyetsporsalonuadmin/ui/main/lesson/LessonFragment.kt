package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson

import android.util.Log
import com.example.cumhuriyetsporsalonuadmin.databinding.FragmentLessonBinding
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseFragment
import com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.adapter.LessonAdapter
import com.example.cumhuriyetsporsalonuadmin.ui.main.all_student_listing.AllStudentListingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LessonFragment : BaseFragment<LessonActionBus, LessonViewModel, FragmentLessonBinding>(
    FragmentLessonBinding::inflate, LessonViewModel::class.java
) {
    private lateinit var adapter: LessonAdapter
    override suspend fun onAction(action: LessonActionBus) {
        when (action) {
            LessonActionBus.Init -> {}
            is LessonActionBus.ShowError -> {
                progressBar.hide()
            }

            is LessonActionBus.ClassesLoaded -> {
                adapter.submitList(viewModel.lessonList)
                Log.d(TAG, "onAction: ${viewModel.lessonList.count()}")
                progressBar.hide()
            }

        }
    }

    override fun initPage() {
        viewModel.getClasses()
        setupRV()
        setOnClickListeners()
    }

    private fun setupRV() {
        adapter = LessonAdapter {
            val action =
                LessonFragmentDirections.actionLessonFragmentToStudentListingFragment(it.uid)
            navigateTo(action)
        }
        binding.rvLesson.adapter = adapter
    }

    private fun setOnClickListeners() {
        binding.apply {
            imgPlus.setOnClickListener {
                val action = LessonFragmentDirections.actionLessonFragmentToAddLessonFragment()
                navigateTo(action)
        val allStudentListingFragment = AllStudentListingFragment()
                
            }
        }
    }

}