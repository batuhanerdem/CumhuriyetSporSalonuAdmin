package com.example.cumhuriyetsporsalonuadmin.ui.main.all_student_listing.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.cumhuriyetsporsalonuadmin.databinding.ItemStudentAddBinding
import com.example.cumhuriyetsporsalonuadmin.databinding.ItemStudentBinding
import com.example.cumhuriyetsporsalonuadmin.databinding.ItemStudentRemoveBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.domain.model.StudentViewHolderTypes
import com.example.cumhuriyetsporsalonuadmin.domain.model.StudentViewHolderTypes.ADDING
import com.example.cumhuriyetsporsalonuadmin.domain.model.StudentViewHolderTypes.LISTING
import com.example.cumhuriyetsporsalonuadmin.domain.model.StudentViewHolderTypes.REMOVING
import com.example.cumhuriyetsporsalonuadmin.utils.SelectableData

class StudentAdapter(
    private var type: StudentViewHolderTypes,
    private val studentOnClick: ((Student) -> Unit) = {},
    private val removeStudent: ((studentUid: String) -> Unit) = {},
    private val addStudent: (SelectableData<Student>, Index) -> Unit = { _, _: Index -> },
) : ListAdapter<SelectableData<Student>, StudentViewHolder>(StudentDiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        return when (type) {
            LISTING -> {
                val binding =
                    ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                StudentListingViewHolder(
                    binding, studentOnClick
                )
            }

            ADDING -> {
                val binding = ItemStudentAddBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                StudentSelectingViewHolder(binding, addStudent)
            }

            REMOVING -> {
                val binding = ItemStudentRemoveBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                StudentRemoveViewHolder(binding, removeStudent)
            }
        }

    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val currentStudent = getItem(position)
        holder.bind(currentStudent)
        if (holder is StudentSelectingViewHolder) {
            holder.selectStudent(currentStudent, position)
        }
    }

    companion object StudentDiffCallback : DiffUtil.ItemCallback<SelectableData<Student>>() {

        var isViewTypeChanged = false
        override fun areItemsTheSame(
            oldItem: SelectableData<Student>, newItem: SelectableData<Student>
        ): Boolean {
            val refreshItems = (oldItem == newItem) && !isViewTypeChanged

            return refreshItems
        }

        override fun areContentsTheSame(
            oldItem: SelectableData<Student>, newItem: SelectableData<Student>
        ): Boolean {
            val refreshItems = (oldItem.isSelected == newItem.isSelected) && !isViewTypeChanged
            isViewTypeChanged = false
            return refreshItems
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (type) {
            LISTING -> LISTING.ordinal
            ADDING -> ADDING.ordinal
            REMOVING -> REMOVING.ordinal
        }
    }

    fun setViewHolderType(viewHolderType: StudentViewHolderTypes) {
        this.type = viewHolderType
        isViewTypeChanged = true
    }

}

const val TAG = "tag"

typealias Index = Int