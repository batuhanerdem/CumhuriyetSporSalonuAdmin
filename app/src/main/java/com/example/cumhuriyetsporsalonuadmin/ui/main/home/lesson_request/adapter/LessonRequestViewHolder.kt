package com.example.cumhuriyetsporsalonuadmin.ui.main.home.lesson_request.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.cumhuriyetsporsalonuadmin.databinding.ItemLessonRequestBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.LessonRequest
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student

class LessonRequestViewHolder(val binding: ItemLessonRequestBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(lessonRequest: LessonRequest) {
        binding.apply {
            val context = tvDay.context
            tvDay.text = lessonRequest.lesson.lessonDate.day.stringIdAsStringfy.getString(context)
            tvLessonName.text = lessonRequest.lesson.name
            tvLessonStartHour.text = lessonRequest.lesson.toFirebaseLesson().startHour
            tvLessonEndHour.text = lessonRequest.lesson.toFirebaseLesson().endHour
            tvStudentName.text = lessonRequest.student.name
        }
    }

    fun setOnClickListeners(
        lessonRequest: LessonRequest,
        goToLesson: (lesson: Lesson) -> Unit,
        goToStudent: (student: Student) -> Unit,
        answerRequest: (lessonRequest: LessonRequest, isAccepted: Boolean) -> Unit,

        ) {
        binding.apply {
            tvGoToLesson.setOnClickListener {
                goToLesson(lessonRequest.lesson)
            }
            tvGoToProfile.setOnClickListener {
                goToStudent(lessonRequest.student)
            }
            btnAccept.setOnClickListener {
                answerRequest(lessonRequest, true)
            }
            btnDeny.setOnClickListener {
                answerRequest(lessonRequest, false)
            }
        }
    }
}