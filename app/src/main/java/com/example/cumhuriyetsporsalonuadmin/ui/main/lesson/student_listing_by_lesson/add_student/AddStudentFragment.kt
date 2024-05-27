package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.student_listing_by_lesson.add_student

import androidx.navigation.fragment.navArgs
import com.example.cumhuriyetsporsalonuadmin.databinding.FragmentAddStudentBinding
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseFragment
import com.example.cumhuriyetsporsalonuadmin.ui.main.all_student_listing.adapter.StudentAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddStudentFragment :
    BaseFragment<AddStudentActionBus, AddStudentViewModel, FragmentAddStudentBinding>(
        FragmentAddStudentBinding::inflate, AddStudentViewModel::class.java
    ) {

    private val args: AddStudentFragmentArgs by navArgs()
    private lateinit var studentAdapter: StudentAdapter

    override suspend fun onAction(action: AddStudentActionBus) {
        when (action) {
            AddStudentActionBus.Init -> {}
            is AddStudentActionBus.ShowError -> {}
            AddStudentActionBus.StudentsLoaded -> {
                studentAdapter.submitList(viewModel.studentList)
            }
        }
    }

    override fun initPage() {
        val lessonUid = args.lessonUid
        setRV()
        viewModel.getStudents(lessonUid)
    }

    private fun setRV() {
        studentAdapter = StudentAdapter()
        binding.rvStudent.adapter = studentAdapter
    }

}