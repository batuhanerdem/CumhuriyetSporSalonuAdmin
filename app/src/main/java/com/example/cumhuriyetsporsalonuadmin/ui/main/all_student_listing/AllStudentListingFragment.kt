package com.example.cumhuriyetsporsalonuadmin.ui.main.all_student_listing

import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.example.cumhuriyetsporsalonuadmin.R
import com.example.cumhuriyetsporsalonuadmin.databinding.FragmentAllStudentListingBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseFragment
import com.example.cumhuriyetsporsalonuadmin.ui.main.all_student_listing.adapter.StudentAdapter
import com.example.cumhuriyetsporsalonuadmin.utils.SelectableData.Companion.toSelectable
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy.Companion.stringfy
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AllStudentListingFragment :
    BaseFragment<AllStudentListingActionBus, AllStudentListingViewModel, FragmentAllStudentListingBinding>(
        FragmentAllStudentListingBinding::inflate, AllStudentListingViewModel::class.java
    ) {
    private lateinit var studentAdapter: StudentAdapter
    override suspend fun onAction(action: AllStudentListingActionBus) {
        when (action) {
            AllStudentListingActionBus.Init -> {}
            is AllStudentListingActionBus.ShowError -> {
                val message = action.error ?: R.string.default_error_message.stringfy()
                showErrorMessage(message)
            }

            AllStudentListingActionBus.StudentsLoaded -> {
                studentAdapter.submitList(viewModel.studentList.toSelectable())
                binding.noStudentFound.isVisible = viewModel.studentList.isEmpty()
            }

            AllStudentListingActionBus.LessonLoaded -> {
                setTitleByLesson(viewModel.lesson)
            }
        }
    }

    override fun initPage() {
        viewModel.getStudents()
        setOnClickListeners()
        setRV()
    }

    private fun setOnClickListeners() {

    }

    private fun setRV() {
        studentAdapter = StudentAdapter(false)
        binding.rvStudent.adapter = studentAdapter
    }

    private fun setTitleByLesson(lesson: Lesson) {
        val titleString = "${lesson.name} - ${lesson.studentUids.count()}"
        binding.tvTitle.text = titleString
    }



}