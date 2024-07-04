package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.cumhuriyetsporsalonuadmin.databinding.ItemLessonListingBinding
import com.example.cumhuriyetsporsalonuadmin.databinding.ItemLessonRemovingBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.LessonViewHolderTypes
import com.example.cumhuriyetsporsalonuadmin.domain.model.LessonViewHolderTypes.LISTING
import com.example.cumhuriyetsporsalonuadmin.domain.model.LessonViewHolderTypes.REMOVING
import com.example.cumhuriyetsporsalonuadmin.ui.main.all_student_listing.adapter.StudentAdapter

class LessonAdapter(
    private val removeStudent: (lessonUid: String) -> Unit = {},
    private val lessonOnClick: (lesson: Lesson) -> Unit,
) : ListAdapter<Lesson, LessonViewHolder>(LessonDiffCallback) {
    private var type: LessonViewHolderTypes = LISTING


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        when (type) {
            LISTING -> {
                val binding = ItemLessonListingBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return LessonListingViewHolder(binding)
            }

            REMOVING -> {
                val binding = ItemLessonRemovingBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return LessonRemovingViewHolder(binding)
            }
        }

    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        val currentLesson = getItem(position)
        when (type) {
            LISTING -> {
                holder as LessonListingViewHolder
                holder.bind(currentLesson)
                holder.binding.layoutInnerConstraint.setOnClickListener {
                    lessonOnClick(currentLesson)
                }
            }

            REMOVING -> {
                holder as LessonRemovingViewHolder
                holder.bind(currentLesson)
                holder.binding.imgStudentRemove.setOnClickListener {
                    removeStudent(currentLesson.uid)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (type) {
            LISTING -> LISTING.ordinal
            REMOVING -> REMOVING.ordinal
        }
    }

    companion object LessonDiffCallback : DiffUtil.ItemCallback<Lesson>() {
        var isViewTypeChanged = false

        override fun areItemsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
            val refreshItems = (oldItem == newItem) && !StudentAdapter.isViewTypeChanged

            return refreshItems
        }

        override fun areContentsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
            val refreshItems = (oldItem.uid == newItem.uid) && !StudentAdapter.isViewTypeChanged
            StudentAdapter.isViewTypeChanged = false
            return refreshItems
        }
    }

    fun setViewHolderType(viewHolderType: LessonViewHolderTypes) {
        this.type = viewHolderType
        isViewTypeChanged = true
    }

}