package com.example.cumhuriyetsporsalonuadmin.ui.main.all_student_listing.adapter

import com.example.cumhuriyetsporsalonuadmin.databinding.ItemStudentRemoveBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.utils.SelectableData

class StudentRemoveViewHolder(
    val binding: ItemStudentRemoveBinding, val studentOnClick: (studentUid: String) -> Unit
) : StudentViewHolder(binding) {
    override fun bind(student: SelectableData<Student>) {
        binding.apply {
            "${student.data.name} ${student.data.surname}".also { tvName.text = it }
            tvEmail.text = student.data.email
        }
        binding.root.setOnClickListener {
            studentOnClick(student.data.uid)
        }
    }
}