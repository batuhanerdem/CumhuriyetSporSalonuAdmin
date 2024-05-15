package com.example.cumhuriyetsporsalonuadmin.ui.main.student_listing.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cumhuriyetsporsalonuadmin.databinding.ItemLessonBinding
import com.example.cumhuriyetsporsalonuadmin.databinding.ItemStudentBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.User

class StudentAdapter(
) : ListAdapter<User, StudentAdapter.StudentViewHolder>(StudentDiffCallback) {
    class StudentViewHolder(val binding: ItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(student: User) {
            binding.apply {
                tvName.text = student.name
                tvEmail.text = student.email
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val currentLesson = getItem(position)
        holder.bind(currentLesson)
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