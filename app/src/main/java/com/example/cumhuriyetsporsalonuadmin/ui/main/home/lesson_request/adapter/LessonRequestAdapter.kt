package com.example.cumhuriyetsporsalonuadmin.ui.main.home.lesson_request.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.cumhuriyetsporsalonuadmin.databinding.ItemLessonRequestBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.LessonRequest
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student


class LessonRequestAdapter(
    private val goToLesson: (lesson: Lesson) -> Unit,
    private val goToStudent: (student: Student) -> Unit,
    private val answerRequest: (lessonRequest: LessonRequest, isAccepted: Boolean) -> Unit,
) : ListAdapter<LessonRequest, LessonRequestViewHolder>(UserDiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonRequestViewHolder {
        val binding =
            ItemLessonRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LessonRequestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LessonRequestViewHolder, position: Int) {
        val currentRequest = getItem(position)
        holder.bind(currentRequest)
        holder.setOnClickListeners(currentRequest, goToLesson, goToStudent, answerRequest)
    }

    object UserDiffCallback : DiffUtil.ItemCallback<LessonRequest>() {
        override fun areItemsTheSame(oldItem: LessonRequest, newItem: LessonRequest): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: LessonRequest, newItem: LessonRequest): Boolean {
            return oldItem == newItem
        }
    }

}