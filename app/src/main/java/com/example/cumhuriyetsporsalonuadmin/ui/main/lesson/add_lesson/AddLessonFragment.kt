package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.add_lesson

import android.util.Log
import com.example.cumhuriyetsporsalonuadmin.databinding.FragmentAddLessonBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.LessonDate
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseFragment
import com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.adapter.DayAdapter
import com.example.cumhuriyetsporsalonuadmin.utils.NullOrEmptyValidator
import com.example.cumhuriyetsporsalonuadmin.utils.LocalTimeConverter.toLocalTime
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy.Companion.stringfy
import dagger.hilt.android.AndroidEntryPoint
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
    }

    override suspend fun onAction(action: AddLessonActionBus) {
        when (action) {
            AddLessonActionBus.Init -> {}
            AddLessonActionBus.Loading -> progressBar.show()
            is AddLessonActionBus.ShowError -> showErrorMessage(action.errorMessage)
            AddLessonActionBus.Success -> showSuccessMessage("lesson saved".stringfy())
            AddLessonActionBus.DayListGenerated -> adapter.submitList(viewModel.selectAbleDayList.toList())
        }
    }

    private fun setOnClickListeners() {
        binding.apply {
            btnAdd.setOnClickListener {
                val name = edtName.text.toString()
                val hourStart = edtStart.text.toString()
                val hourEnd = edtEnd.text.toString()
                val selectedDayList = viewModel.getSelectedDaysList()
                val uid = generateUUIDString()
                val isNotEmpty =
                    NullOrEmptyValidator.validate(name, hourStart, hourEnd, selectedDayList)
                if (!isNotEmpty) {
                    handleEmptyForm()
                    return@setOnClickListener
                }
                val firstHour = hourStart.toLocalTime()
                val secondHour = hourEnd.toLocalTime()
                if (firstHour == null || secondHour == null) {
                    showErrorMessage("Lutfen tarihlerin formatini kontrol edin".stringfy())
                    return@setOnClickListener
                }
                val lessonDate = LessonDate(selectedDayList[0].data, firstHour, secondHour)
                val lesson = Lesson(uid, name, lessonDate)
                viewModel.saveLesson(lesson)
            }
            tvCancel.setOnClickListener {
                navigateBack()
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

    private fun handleEmptyForm() {
        showErrorMessage("Please fill all the blanks and select at least 1 day".stringfy())
    }

    private fun generateUUIDString() = UUID.randomUUID().toString()
}