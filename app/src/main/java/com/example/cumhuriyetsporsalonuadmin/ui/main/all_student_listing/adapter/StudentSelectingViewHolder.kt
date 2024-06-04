package com.example.cumhuriyetsporsalonuadmin.ui.main.all_student_listing.adapter

import com.example.cumhuriyetsporsalonuadmin.R
import com.example.cumhuriyetsporsalonuadmin.databinding.ItemStudentAddBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.utils.SelectableData

class StudentSelectingViewHolder(
    private val binding: ItemStudentAddBinding,
    private val addStudent: (SelectableData<Student>, Index) -> Unit
) : StudentViewHolder(binding) {

    override fun bind(student: SelectableData<Student>) {
        binding.apply {
            tvName.text = student.data.name
            tvEmail.text = student.data.email
            val resId =
                if (student.isSelected) R.drawable.ic_check_green else R.drawable.ic_check_gray
            imgAddStudent.setImageResource(resId)
        }
    }

    fun selectStudent(student: SelectableData<Student>, position: Int) {
        binding.root.setOnClickListener {
            addStudent(student, position)
        }
    }
}