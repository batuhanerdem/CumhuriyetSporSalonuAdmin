package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.cumhuriyetsporsalonuadmin.databinding.ItemLessonRemovingBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson

class LessonRemovingAdapter(
    private val removeStudent: (lessonUid: String) -> Unit = {},
) : ListAdapter<Lesson, LessonRemovingViewHolder>(LessonDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonRemovingViewHolder {

        val binding = ItemLessonRemovingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LessonRemovingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LessonRemovingViewHolder, position: Int) {
        val currentLesson = getItem(position)


        holder.bind(currentLesson)
        holder.binding.imgStudentRemove.setOnClickListener {
            removeStudent(currentLesson.uid)
        }
    }

    companion object LessonDiffCallback : DiffUtil.ItemCallback<Lesson>() {

        override fun areItemsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
            return oldItem.uid == newItem.uid
        }
    }

}