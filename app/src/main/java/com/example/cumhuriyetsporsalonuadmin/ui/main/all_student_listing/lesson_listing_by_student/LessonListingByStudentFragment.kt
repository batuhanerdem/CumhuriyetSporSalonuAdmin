package com.example.cumhuriyetsporsalonuadmin.ui.main.all_student_listing.lesson_listing_by_student

import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.cumhuriyetsporsalonuadmin.databinding.FragmentLessonListingByStudentBinding
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseFragment
import com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.adapter.LessonAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LessonListingByStudentFragment :
    BaseFragment<LessonListingByStudentActionBus, LessonListingByStudentViewModel, FragmentLessonListingByStudentBinding>(
        FragmentLessonListingByStudentBinding::inflate, LessonListingByStudentViewModel::class.java
    ) {
    private lateinit var adapter: LessonAdapter
    private val args: LessonListingByStudentFragmentArgs by navArgs()

    override suspend fun onAction(action: LessonListingByStudentActionBus) {
        when (action) {
            LessonListingByStudentActionBus.Init -> {}
            is LessonListingByStudentActionBus.ShowError -> {
                Toast.makeText(
                    requireContext(), action.error?.getString(requireContext()), Toast.LENGTH_SHORT
                ).show()
            }

            is LessonListingByStudentActionBus.ClassesLoaded -> adapter.submitList(viewModel.lessonList)

            LessonListingByStudentActionBus.StudentLoaded -> {
                binding.tvStudentName.text = viewModel.student.name
            }
        }
    }

    override fun initPage() {
        val studentUid = args.studentUid
        viewModel.getClasses(studentUid)
        viewModel.getStudent(studentUid)
        setupRV()
        setOnClickListeners()
    }

    private fun setupRV() {
        adapter = LessonAdapter {
            val action =
                LessonListingByStudentFragmentDirections.actionLessonListingByStudentToStudentListingByLessonFragment(
                    it.uid
                )
            navigateTo(action)
        }
        binding.rvLesson.adapter = adapter
    }

    private fun setOnClickListeners() {
        binding.imgBack.setOnClickListener {
            navigateBack()
        }

    }

}