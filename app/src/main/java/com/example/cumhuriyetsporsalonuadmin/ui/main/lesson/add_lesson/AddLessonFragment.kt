package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.add_lesson

import android.app.TimePickerDialog
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.cumhuriyetsporsalonuadmin.R
import com.example.cumhuriyetsporsalonuadmin.databinding.FragmentAddLessonBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Days
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.LessonDate
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseFragment
import com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.adapter.DayAdapter
import com.example.cumhuriyetsporsalonuadmin.utils.NullOrEmptyValidator
import com.example.cumhuriyetsporsalonuadmin.utils.LocalTimeConverter.toLocalTime
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy.Companion.stringfy
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalTime
import java.util.UUID

@AndroidEntryPoint
class AddLessonFragment :
    BaseFragment<AddLessonActionBus, AddLessonViewModel, FragmentAddLessonBinding>(
        FragmentAddLessonBinding::inflate, AddLessonViewModel::class.java
    ) {
    private lateinit var adapter: DayAdapter

    override fun initPage() {
        setRV()
        viewModel.generateDayList()
        setOnClickListeners()
//        viewModel.deleteAllLessons()
    }

    override suspend fun onAction(action: AddLessonActionBus) {
        when (action) {
            AddLessonActionBus.Init -> {}
            AddLessonActionBus.Loading -> progressBar.show()
            is AddLessonActionBus.ShowError -> showErrorMessage(action.errorMessage)
            AddLessonActionBus.Success -> {
                val lessonSavedString = getString(R.string.lesson_saved)
                showSuccessMessage(lessonSavedString.stringfy())
            }

            AddLessonActionBus.DayListGenerated -> {
                progressBar.hide()
                adapter.submitList(viewModel.selectAbleDayList.toList())
            }

            AddLessonActionBus.PageCleared -> {
                viewModel.generateDayList()
                adapter.submitList(viewModel.selectAbleDayList.toList())
            }

        }
    }

    private fun setOnClickListeners() {
        val timePickerListenerForStart =
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                setHoursTextView(binding.tvStartShow, hourOfDay, minute)
            }

        val timePickerListenerForEnd = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            setHoursTextView(binding.tvEndShow, hourOfDay, minute)

        }


        binding.apply {
            btnAdd.setOnClickListener {
                val name = edtName.text.toString()
                val hourStart = tvStartShow.text.toString()
                val hourEnd = tvEndShow.text.toString()
                val selectedDayList = viewModel.getSelectedDaysList()

                val isNotEmpty =
                    NullOrEmptyValidator.validate(name, hourStart, hourEnd, selectedDayList)
                if (!isNotEmpty) {
                    handleEmptyForm()
                    return@setOnClickListener
                }
                val firstHour = hourStart.toLocalTime()
                val secondHour = hourEnd.toLocalTime()
                if (firstHour == null || secondHour == null) return@setOnClickListener

                if (!viewModel.isFirstHourBeforeSecondHour(
                        firstHour, secondHour
                    )
                ) {
                    val secondHourShouldBeAfterString =
                        getString(R.string.second_hour_should_be_after)
                    Toast.makeText(
                        requireContext(), secondHourShouldBeAfterString, Toast.LENGTH_SHORT
                    ).show()
                    showErrorMessage(secondHourShouldBeAfterString.stringfy())
                    return@setOnClickListener
                }

                saveLesson(selectedDayList, firstHour, secondHour, name)
            }
            tvCancel.setOnClickListener {
                navigateBack()
            }

            imgStartHour.setOnClickListener {
                showTimePicker(timePickerListenerForStart)
            }

            tvStartShow.setOnClickListener {
                showTimePicker(timePickerListenerForStart)
            }

            imgEndHour.setOnClickListener {
                showTimePicker(timePickerListenerForEnd)
            }

            tvEndShow.setOnClickListener {
                showTimePicker(timePickerListenerForEnd)
            }


        }

    }

    private fun setRV() {
        adapter = DayAdapter { day, index ->
            val newDayInstance = day.getReversed()
            viewModel.selectAbleDayList[index] = newDayInstance
            Log.d(TAG, "setRV: day: ${day}, newDay: ${newDayInstance}")
            adapter.submitList(viewModel.selectAbleDayList.toList())

        }
        binding.rvDay.adapter = adapter
    }

    private fun saveLesson(
        selectedDayList: List<Days>, firstHour: LocalTime, secondHour: LocalTime, name: String
    ) {
        for (day in selectedDayList) {
            val lessonDate = LessonDate(day, firstHour, secondHour)
            val lesson = Lesson(generateUUID(), name, lessonDate)
            viewModel.saveLesson(lesson)
            clearTexts()
        }
    }

    private fun handleEmptyForm() {
        val fillTheBlanksString = getString(R.string.fill_the_blanks)
        showErrorMessage(fillTheBlanksString.stringfy())
        Toast.makeText(requireContext(), fillTheBlanksString, Toast.LENGTH_SHORT).show()
    }

    private fun clearTexts() {
        binding.apply {
            edtName.text.clear()
            tvStartShow.text = null
            tvEndShow.text = null
        }
        viewModel.clearDays()
    }

    private fun showTimePicker(listener: TimePickerDialog.OnTimeSetListener) {
        TimePickerDialog(requireContext(), listener, 12, 0, true).show()
    }

    private fun setHoursTextView(
        textView: TextView, hourOfDay: Int, minute: Int
    ) {
        val editedHour = if (hourOfDay < 10) "0$hourOfDay" else hourOfDay
        val editedMinute = if (minute < 10) "0$minute" else minute
        "$editedHour.$editedMinute".also { textView.text = it }
    }

    private fun generateUUID() = UUID.randomUUID().toString()
}