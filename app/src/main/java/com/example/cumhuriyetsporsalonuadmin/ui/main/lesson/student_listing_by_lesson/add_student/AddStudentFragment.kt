package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.student_listing_by_lesson.add_student

import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.example.cumhuriyetsporsalonuadmin.R
import com.example.cumhuriyetsporsalonuadmin.databinding.FragmentAddStudentBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.StudentViewHolderTypes
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseFragment
import com.example.cumhuriyetsporsalonuadmin.ui.main.all_student_listing.adapter.StudentAdapter
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy.Companion.stringfy
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
//                studentAdapter.submitList(viewModel.selectableStudentList)
//                setTvVisibility(viewModel.selectableStudentList.value?.isEmpty())
            }

            AddStudentActionBus.StudentsAdded -> {
                showSuccessMessage(R.string.student_saved.stringfy())
//                viewModel.getStudents()
//                setBtnSpecs(viewModel.getSelectedStudents().count())
//                setTvVisibility(viewModel.selectableStudentList.isEmpty())
                navigateBack()
            }

            AddStudentActionBus.LessonNameLoaded -> {
                setLessonName(viewModel.lessonName)
            }
        }
    }

    override fun initPage() {
        viewModel.args = args
        setRV()
        setObserver()
        viewModel.getLessonName()
        viewModel.getStudents()
        setOnClickListeners()
    }

    private fun setRV() {
        studentAdapter = StudentAdapter(StudentViewHolderTypes.ADDING) { student, index ->
            val newStudentInstance = student.getReversed()

            val updatedList = viewModel.selectableStudentList.value?.toMutableList()
            updatedList?.set(index, newStudentInstance)

            viewModel.selectableStudentList.value = updatedList

        }
        binding.rvStudent.adapter = studentAdapter
    }

    private fun setOnClickListeners() {
        binding.apply {
            btnAdd.setOnClickListener {
                viewModel.addStudent(viewModel.getSelectedStudents())
            }
        }
    }

    private fun setObserver() {
        viewModel.selectableStudentList.observe(viewLifecycleOwner) {
            studentAdapter.submitList(it)
            setTvVisibility(it.isEmpty())
            setBtnSpecs(viewModel.getSelectedStudents().count())

        }
    }

    private fun setLessonName(lessonName: String) {
        binding.tvTitle.text = lessonName
    }


    private fun setBtnSpecs(studentCount: Int) {
        if (studentCount == 0) {
            binding.btnAdd.isClickable = false
            " ${
                R.string.add_students.stringfy().getString(requireContext())
            }".also { binding.btnAdd.text = it }
            return
        }
        binding.btnAdd.isClickable = true
        " ${
            R.string.add_students.stringfy().getString(requireContext())
        } ($studentCount)".also { binding.btnAdd.text = it }
    }

    private fun setTvVisibility(isVisible: Boolean) {
        binding.noStudentFound.isVisible = isVisible
    }
}