package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.cumhuriyetsporsalonuadmin.databinding.ItemLessonListingBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson

class LessonAdapter(
    private val lessonOnClick: (lesson: Lesson) -> Unit,
) : ListAdapter<Lesson, LessonListingViewHolder>(LessonDiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonListingViewHolder {
        val binding = ItemLessonListingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LessonListingViewHolder(binding)
    }


    override fun onBindViewHolder(holder: LessonListingViewHolder, position: Int) {
        val currentLesson = getItem(position)
        holder.bind(currentLesson)
        holder.binding.layoutInnerConstraint.setOnClickListener {
            lessonOnClick(currentLesson)
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