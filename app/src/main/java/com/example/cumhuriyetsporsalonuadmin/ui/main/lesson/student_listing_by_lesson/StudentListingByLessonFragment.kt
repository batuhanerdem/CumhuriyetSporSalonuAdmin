package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.student_listing_by_lesson

import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.example.cumhuriyetsporsalonuadmin.R
import com.example.cumhuriyetsporsalonuadmin.databinding.FragmentStudentListingByLessonBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.domain.model.StudentViewHolderTypes
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseFragment
import com.example.cumhuriyetsporsalonuadmin.ui.main.all_student_listing.adapter.StudentAdapter
import com.example.cumhuriyetsporsalonuadmin.utils.SelectableData.Companion.toSelectable
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy.Companion.stringfy
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class StudentListingByLessonFragment :
    BaseFragment<StudentListingByLessonActionBus, StudentListingByLessonViewModel, FragmentStudentListingByLessonBinding>(
        FragmentStudentListingByLessonBinding::inflate, StudentListingByLessonViewModel::class.java
    ) {
    private lateinit var studentAdapter: StudentAdapter
    private val args: StudentListingByLessonFragmentArgs by navArgs()

    override suspend fun onAction(action: StudentListingByLessonActionBus) {
        when (action) {
            StudentListingByLessonActionBus.Init -> {}
            is StudentListingByLessonActionBus.ShowError -> {
                val message = action.error
                showErrorMessage(message)
            }

            StudentListingByLessonActionBus.StudentsLoaded -> {
                studentAdapter.submitList(viewModel.studentList.toSelectable())
                setTvVisibility(viewModel.studentList.isEmpty())
            }

            StudentListingByLessonActionBus.LessonLoaded -> {
                setTitleByLesson(viewModel.lesson)
            }

            StudentListingByLessonActionBus.StudentRemoved -> {
                showSuccessMessage(R.string.student_removed.stringfy())
                studentAdapter.submitList(viewModel.studentList.toSelectable())
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
        val lessonUid = args.lessonUid
        binding.imgAdd.setOnClickListener {
            val action =
                StudentListingByLessonFragmentDirections.actionStudentListingByLessonFragmentToAddStudentFragment(
                    lessonUid
                )
            navigateTo(action)
        }
        binding.tvEdit.setOnClickListener {
            setEditingMode(true)
            studentAdapter.setViewHolderType(StudentViewHolderTypes.REMOVING)
            studentAdapter.submitList(viewModel.studentList.toSelectable().toList())


        }
        binding.tvShow.setOnClickListener {
            setEditingMode(false)
            studentAdapter.setViewHolderType(StudentViewHolderTypes.LISTING)
            studentAdapter.submitList(viewModel.studentList.toSelectable().toList())

        }
    }

    private fun setRV() {
        studentAdapter = StudentAdapter(
            StudentViewHolderTypes.LISTING,
            studentOnClick = ::goToStudent,
            removeStudent = ::deleteStudentFromLesson
        )
        binding.rvStudent.adapter = studentAdapter
    }

    private fun goToStudent(student: Student) {
        val action =
            StudentListingByLessonFragmentDirections.actionStudentListingByLessonFragmentToStudentProfileFragment(
                student.uid
            )
        navigateTo(action)
    }

    private fun deleteStudentFromLesson(studentUid: String) {
        viewModel.deleteStudentFromLesson(studentUid)
    }

    private fun setTitleByLesson(lesson: Lesson) {
        val titleString = "${lesson.name} - ${lesson.studentUids.count()}"
        binding.tvTitle.text = titleString
    }

    private fun setPageByLessonUID(uid: String) {
        viewModel.getLessonByUID(uid)
    }

    private fun setTvVisibility(isVisible: Boolean) {
        binding.noStudentFound.isVisible = isVisible
        binding.tvEdit.isVisible = !isVisible
    }

    private fun setEditingMode(isEditing: Boolean) {
        binding.tvEdit.isVisible = !isEditing
        binding.tvShow.isVisible = isEditing
    }
}