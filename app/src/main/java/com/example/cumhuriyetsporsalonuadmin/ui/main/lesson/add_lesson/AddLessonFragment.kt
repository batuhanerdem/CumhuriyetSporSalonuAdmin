package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.add_lesson

import com.example.cumhuriyetsporsalonuadmin.databinding.FragmentAddLessonBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseFragment
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy.Companion.stringfy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

@AndroidEntryPoint
class AddLessonFragment :
    BaseFragment<AddLessonActionBus, AddLessonViewModel, FragmentAddLessonBinding>(
        FragmentAddLessonBinding::inflate, AddLessonViewModel::class.java
    ) {

    override fun initPage() {
        setOnClickListeners()
    }

    override suspend fun onAction(action: AddLessonActionBus) {
        when (action) {
            AddLessonActionBus.Init -> {}
            AddLessonActionBus.Loading -> progressBar.show()
            is AddLessonActionBus.ShowError -> showErrorMessage(action.errorMessage)
            AddLessonActionBus.Success -> showSuccessMessage("lesson saved".stringfy())
        }
    }

    private fun setOnClickListeners() {
        binding.apply {
            btnAdd.setOnClickListener {
                val name = edtName.text.toString()
                val hourStart = edtHourStart.text.toString()
                val hourEnd = edtHourEnd.text.toString()
                val day = edtDay.text.toString()
                val uid = UUID.randomUUID().toString()
                val lesson = Lesson(uid, name, day, hourStart, hourEnd)

                viewModel.saveLesson(lesson)
//
//                GlobalScope.launch {
//                    delay(10000)
//                    viewModel.deleteLesson(uid)
//                }
            }
        }
    }
}