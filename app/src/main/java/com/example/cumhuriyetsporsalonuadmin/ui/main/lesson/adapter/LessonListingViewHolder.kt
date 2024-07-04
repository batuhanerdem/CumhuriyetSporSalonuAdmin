package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.adapter

import com.example.cumhuriyetsporsalonuadmin.databinding.ItemLessonListingBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson

class LessonListingViewHolder(val binding: ItemLessonListingBinding) :
    LessonViewHolder(binding) {
    override fun bind(lesson: Lesson) {
        val context = binding.tvName.context
        binding.apply {
            val dayText = lesson.lessonDate?.day?.stringIdAsStringfy?.getString(context)
            tvName.text = lesson.name
            val hoursText =
                "${lesson.lessonDate?.startHour.toString()} - ${lesson.lessonDate?.endHour.toString()}"

            "$dayText, $hoursText".also { tvDate.text = it }
            tvStudentNumber.text = lesson.studentUids.count().toString()
        }
    }
}