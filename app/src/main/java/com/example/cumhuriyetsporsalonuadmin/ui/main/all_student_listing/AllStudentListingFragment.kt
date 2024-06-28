package com.example.cumhuriyetsporsalonuadmin.ui.main.all_student_listing

import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.example.cumhuriyetsporsalonuadmin.R
import com.example.cumhuriyetsporsalonuadmin.databinding.FragmentAllStudentListingBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
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
                setVisibility(viewModel.studentList.isEmpty())
            }

            AllStudentListingActionBus.LessonLoaded -> {
                setTitleByLesson(viewModel.lesson)
            }

            AllStudentListingActionBus.StudentsFiltered -> {
                studentAdapter.submitList(viewModel.filteredList.toSelectable())
            }
        }
    }

    override fun initPage() {
        viewModel.getStudents()
        setOnClickListeners()
        setRV()
        setSearchListener()
    }

    private fun setOnClickListeners() {
        binding.apply {
            imgCancel.setOnClickListener {
                edtSearch.text.clear()
            }
        }
    }

    private fun setRV() {
        studentAdapter = StudentAdapter(false, studentOnClick = ::goToStudent)
        binding.rvStudent.adapter = studentAdapter
    }

    private fun setVisibility(isVisible: Boolean) {
        binding.noStudentFound.isVisible = isVisible
        binding.edtSearch.isVisible = !isVisible

    }

    private fun setSearchListener() {
        binding.edtSearch.text.clear()
        binding.edtSearch.doOnTextChanged { text, _, _, _ ->
            changeXButtonVisibility(text.toString().isEmpty())
            viewModel.filterList(text.toString())
        }

    }

    private fun goToStudent(student: Student) {
        val action =
            AllStudentListingFragmentDirections.actionAllStudentListingFragmentToLessonListingByStudent(
                student.uid
            )
        navigateTo(action)
    }

    private fun setTitleByLesson(lesson: Lesson) {
        val titleString = "${lesson.name} - ${lesson.studentUids.count()}"
        binding.tvTitle.text = titleString
    }

    private fun changeXButtonVisibility(visibility: Boolean) {
        binding.imgCancel.isVisible = !visibility
    }

}