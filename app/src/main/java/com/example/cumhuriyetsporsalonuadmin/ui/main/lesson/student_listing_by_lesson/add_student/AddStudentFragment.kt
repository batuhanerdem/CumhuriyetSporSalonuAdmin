package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.student_listing_by_lesson.add_student

import android.util.Log
import androidx.navigation.fragment.navArgs
import com.example.cumhuriyetsporsalonuadmin.R
import com.example.cumhuriyetsporsalonuadmin.databinding.FragmentAddStudentBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseFragment
import com.example.cumhuriyetsporsalonuadmin.ui.main.all_student_listing.adapter.StudentAdapter
import com.example.cumhuriyetsporsalonuadmin.utils.SelectableData
import com.example.cumhuriyetsporsalonuadmin.utils.SelectableData.Companion.toSelectable
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy.Companion.stringfy
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

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
                studentAdapter.submitList(viewModel.selectableStudentList)
            }

            AddStudentActionBus.StudentsAdded -> {
                showSuccessMessage("Student Added".stringfy())
            }
            AddStudentActionBus.LessonNameLoaded -> {
                setLessonName(viewModel.lessonName)
            }
        }
    }

    override fun initPage() {
        val lessonUid = args.lessonUid
        setRV()
        viewModel.getLessonName(lessonUid)
        viewModel.getStudents(lessonUid)
        setOnClickListeners()
    }

    private fun setRV() {
        studentAdapter = StudentAdapter(true) { student, index ->
            val newStudentInstance = student.getReversed()

            // Listenin kopyasını oluşturun ve güncelleyin
            val updatedList = viewModel.selectableStudentList.toMutableList()
            updatedList[index] = newStudentInstance

            // Güncellenmiş listeyi viewModel'e atayın
            viewModel.selectableStudentList = updatedList

            // Adapter'e güncellenmiş listeyi verin ve adapter'i bilgilendirin
            studentAdapter.submitList(viewModel.selectableStudentList.toList())
//            viewModel.getSelectedStudents()
            setBtnSpecs(viewModel.getSelectedStudents().count())
        }
        binding.rvStudent.adapter = studentAdapter
    }

    private fun setOnClickListeners() {
        binding.apply {
            btnAdd.setOnClickListener {
                viewModel.addStudent(args.lessonUid, viewModel.getSelectedStudents())
            }
        }
    }

    private fun setLessonName(lessonName: String) {
        binding.tvTitle.text = lessonName
    }

    private fun addStudent(lessonId: String, studentList: List<Student>) {
        viewModel.addStudent(lessonId, studentList)
    }

    private fun setBtnSpecs(studentCount: Int) {
        " ${
            R.string.add_students.stringfy().getString(requireContext())
        } ($studentCount)".also { binding.btnAdd.text = it }
        binding.btnAdd.isClickable = studentCount != 0
    }
}