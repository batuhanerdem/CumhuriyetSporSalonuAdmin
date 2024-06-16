package com.example.cumhuriyetsporsalonuadmin.ui.main.all_student_listing.adapter

import com.example.cumhuriyetsporsalonuadmin.databinding.ItemStudentBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.utils.SelectableData

class StudentListingViewHolder(
    val binding: ItemStudentBinding, val studentOnClick: (Student) -> Unit
) : StudentViewHolder(binding) {
    override fun bind(student: SelectableData<Student>) {
        binding.apply {
            tvName.text = student.data.name
            tvEmail.text = student.data.email
        }
        binding.root.setOnClickListener {
            studentOnClick(student.data)
        }
    }
}