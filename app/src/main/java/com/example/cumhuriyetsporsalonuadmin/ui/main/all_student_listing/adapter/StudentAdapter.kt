package com.example.cumhuriyetsporsalonuadmin.ui.main.all_student_listing.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.cumhuriyetsporsalonuadmin.databinding.ItemStudentAddBinding
import com.example.cumhuriyetsporsalonuadmin.databinding.ItemStudentBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.utils.SelectableData

class StudentAdapter(
    private val isSelecting: Boolean,
    private val studentOnClick: ((Student) -> Unit)? = null,
    private val addStudent: (SelectableData<Student>, Index) -> Unit = { _, _: Index -> },
) : ListAdapter<SelectableData<Student>, StudentViewHolder>(StudentDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        return if (isSelecting) {
            val binding =
                ItemStudentAddBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            StudentSelectingViewHolder(binding, addStudent)
        } else {
            val binding =
                ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            StudentListingViewHolder(
                binding, studentOnClick!! //fix that sometime
            )
        }
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val currentStudent = getItem(position)
        holder.bind(currentStudent)
        if (holder is StudentSelectingViewHolder) {
            holder.selectStudent(currentStudent, position)
        }
    }

    object StudentDiffCallback : DiffUtil.ItemCallback<SelectableData<Student>>() {
        override fun areItemsTheSame(
            oldItem: SelectableData<Student>, newItem: SelectableData<Student>
        ): Boolean {
            return oldItem == newItem
//            return false //testing
        }

        override fun areContentsTheSame(
            oldItem: SelectableData<Student>, newItem: SelectableData<Student>
        ): Boolean {
//            return false //testing
            return oldItem.isSelected == newItem.isSelected
        }
    }

//    fun test(){
//        this
//    }

}

typealias Index = Int