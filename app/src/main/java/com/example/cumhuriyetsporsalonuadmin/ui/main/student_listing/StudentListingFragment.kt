package com.example.cumhuriyetsporsalonuadmin.ui.main.student_listing

import androidx.core.view.isVisible
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.example.cumhuriyetsporsalonuadmin.R
import com.example.cumhuriyetsporsalonuadmin.databinding.FragmentStudentListingBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseFragment
import com.example.cumhuriyetsporsalonuadmin.ui.main.student_listing.adapter.StudentAdapter
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy.Companion.stringfy
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class StudentListingFragment :
    BaseFragment<StudentListingActionBus, StudentListingViewModel, FragmentStudentListingBinding>(
        FragmentStudentListingBinding::inflate, StudentListingViewModel::class.java
    ) {
    private lateinit var studentAdapter: StudentAdapter
    private val args: StudentListingFragmentArgs by navArgs()
    override suspend fun onAction(action: StudentListingActionBus) {
        when (action) {
            StudentListingActionBus.Init -> {}
            is StudentListingActionBus.ShowError -> {
                val message = action.error ?: R.string.default_error_message.stringfy()
                showErrorMessage(message)
            }

            StudentListingActionBus.StudentsLoaded -> {
                studentAdapter.submitList(viewModel.studentList)
                binding.noStudentFound.isVisible = viewModel.studentList.isEmpty()
            }

            StudentListingActionBus.LessonLoaded -> {
                setTitleByLesson(viewModel.lesson)
            }
        }
    }

    override fun initPage() {
        val lessonUid = args.lessonUid
        viewModel.getStudents(lessonUid)
        setOnClickListeners()
        setPageByLessonUID(lessonUid)
        setRV()
    }

    private fun setOnClickListeners() {

    }

    private fun setRV() {
        studentAdapter = StudentAdapter()
        binding.rvStudent.adapter = studentAdapter
    }

    private fun setTitle() {
        binding.tvTitle.text = getString(R.string.all_students)
    }

    private fun setTitleByLesson(lesson: Lesson) {
        val titleString = "${lesson.name} - ${lesson.studentUids.count()}"
        binding.tvTitle.text = titleString
    }

    private fun setPageByLessonUID(uid: String?) {
        if (uid == null) {
            setTitle()
            setPageAsEditing(false)
        } else {
            viewModel.getLessonByUID(uid)
            setPageAsEditing(true)
        }
    }

    private fun setPageAsEditing(isEditing: Boolean) {
        binding.imgAdd.isVisible = isEditing
        binding.tvEdit.isVisible = isEditing
    }


}