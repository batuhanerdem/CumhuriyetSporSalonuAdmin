package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cumhuriyetsporsalonuadmin.databinding.ItemLessonBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson

class LessonAdapter(
) : ListAdapter<Lesson, LessonAdapter.LessonViewHolder>(LessonDiffCallback) {
    class LessonViewHolder(val binding: ItemLessonBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(lesson: Lesson) {
            val context = binding.tvName.context
            binding.apply {
                val dayText = lesson.lessonDate?.day?.stringIdAsStringfy?.getString(context)
                tvName.text = lesson.name
                val hoursText =
                    "${lesson.lessonDate?.startHour.toString()} - ${lesson.lessonDate?.endHour.toString()}"

                tvDate.text = "$dayText, $hoursText"
                tvStudentNumber.text = lesson.studentUids.count().toString()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val binding = ItemLessonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LessonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        val currentLesson = getItem(position)
        holder.bind(currentLesson)
    }

    object LessonDiffCallback : DiffUtil.ItemCallback<Lesson>() {
        override fun areItemsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
            return oldItem == newItem
//            return false //testing
        }

        override fun areContentsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
            return oldItem.uid == newItem.uid
//            return false //testing
        }
    }

}