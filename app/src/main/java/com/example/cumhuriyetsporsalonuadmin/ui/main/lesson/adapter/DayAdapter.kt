package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cumhuriyetsporsalonuadmin.R
import com.example.cumhuriyetsporsalonuadmin.databinding.ItemDayBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Days
import com.example.cumhuriyetsporsalonuadmin.utils.SelectableData

class DayAdapter(
    private val daySelected: (SelectableData<Days>, Int) -> Unit
) : ListAdapter<SelectableData<Days>, DayAdapter.DaysViewHolder>(DayDiffCallback) {
    class DaysViewHolder(
        val binding: ItemDayBinding, private val daySelected: (SelectableData<Days>, Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(day: SelectableData<Days>) {
            val context = binding.tvDayName.context
            binding.tvDayName.text = day.data.stringIdAsStringfy.getString(context)
            val imgResource =
                if (day.isSelected) R.drawable.ic_check_green else R.drawable.ic_check_gray
            binding.imgCheck.setImageResource(imgResource)

        }

        fun selectItem(
            day: SelectableData<Days>, position: Int
        ) {
            binding.root.setOnClickListener {
                daySelected(day, position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysViewHolder {
        val binding = ItemDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DaysViewHolder(binding, daySelected)
    }

    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) {
        val currentDay = getItem(position)
        holder.bind(currentDay)
        holder.selectItem(currentDay, position)
    }

    object DayDiffCallback : DiffUtil.ItemCallback<SelectableData<Days>>() {
        override fun areItemsTheSame(
            oldItem: SelectableData<Days>, newItem: SelectableData<Days>
        ): Boolean {
            return oldItem.data == newItem.data
//            return false //testing
        }

        override fun areContentsTheSame(
            oldItem: SelectableData<Days>, newItem: SelectableData<Days>
        ): Boolean {
            return oldItem.isSelected == newItem.isSelected
//            return false //testing
        }
    }

}