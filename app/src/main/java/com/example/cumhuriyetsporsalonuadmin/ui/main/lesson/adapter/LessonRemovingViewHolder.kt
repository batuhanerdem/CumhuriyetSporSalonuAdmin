package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.adapter

import com.example.cumhuriyetsporsalonuadmin.databinding.ItemLessonRemovingBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson

class LessonRemovingViewHolder(
    val binding: ItemLessonRemovingBinding,
) : LessonViewHolder(binding) {
    override fun bind(lesson: Lesson) {
        val context = binding.tvName.context
        binding.apply {
            val dayText = lesson.lessonDate?.day?.stringIdAsStringfy?.getString(context)
            tvName.text = lesson.name
            val hoursText =
                "${lesson.lessonDate?.startHour.toString()} - ${lesson.lessonDate?.endHour.toString()}"

            "$dayText, $hoursText".also { tvDate.text = it }
        }

    }
}