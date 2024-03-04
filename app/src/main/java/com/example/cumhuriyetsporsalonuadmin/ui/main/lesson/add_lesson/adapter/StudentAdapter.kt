package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.add_lesson.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cumhuriyetsporsalonuadmin.databinding.ItemStudentBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.User

class StudentAdapter(
) : ListAdapter<User, StudentAdapter.StudentViewHolder>(StudentDiffCallback) {
    class StudentViewHolder(val binding: ItemStudentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lesson: User) {
//            binding.apply {
//                tvDay.text = lesson.day
//                tvName.text = lesson.name
//                tvHours.text = "${lesson.timeBegin} - ${lesson.timeEnd}"
//                tvStudentNumber.text = lesson.studentUids.count().toString()
//            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val currentStudent = getItem(position)
        holder.bind(currentStudent)
    }

    object StudentDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
//            return false //testing
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.uid == newItem.uid
//            return false //testing
        }
    }

}