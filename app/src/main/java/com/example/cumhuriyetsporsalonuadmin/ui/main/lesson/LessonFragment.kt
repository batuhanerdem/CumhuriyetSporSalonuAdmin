package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson

import android.util.Log
import androidx.core.view.isVisible
import com.example.cumhuriyetsporsalonuadmin.R
import com.example.cumhuriyetsporsalonuadmin.databinding.FragmentLessonBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.LessonViewHolderTypes
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.domain.model.StudentViewHolderTypes
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseFragment
import com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.adapter.LessonAdapter
import com.example.cumhuriyetsporsalonuadmin.utils.SelectableData.Companion.toSelectable
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy.Companion.stringfy
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LessonFragment : BaseFragment<LessonActionBus, LessonViewModel, FragmentLessonBinding>(
    FragmentLessonBinding::inflate, LessonViewModel::class.java
) {
    private lateinit var adapter: LessonAdapter
    override suspend fun onAction(action: LessonActionBus) {
        when (action) {
            LessonActionBus.Init -> {}
            is LessonActionBus.ShowError -> {
                showErrorMessage(action.error)
            }

            is LessonActionBus.ClassesLoaded -> {
                adapter.submitList(viewModel.lessonList)
            }

            LessonActionBus.LessonDeleted -> {
                showSuccessMessage(R.string.lesson_removed.stringfy())
                adapter.submitList(viewModel.lessonList)
            }
        }
    }

    override fun initPage() {
        viewModel.getClasses()
        setupRV()
        setOnClickListeners()
    }

    private fun setupRV() {
        adapter = LessonAdapter(removeStudent = ::removeLesson) {
            val action =
                LessonFragmentDirections.actionLessonFragmentToStudentListingByLessonFragment(it.uid)
            navigateTo(action)
        }
        binding.rvLesson.adapter = adapter
    }

    private fun removeLesson(lessonUid: String) {
        viewModel.deleteLesson(lessonUid)
        adapter.submitList(viewModel.lessonList.toList())
    }

    private fun setOnClickListeners() {
        binding.apply {
            imgAdd.setOnClickListener {
                val action = LessonFragmentDirections.actionLessonFragmentToAddLessonFragment()
                navigateTo(action)
            }
        }
        binding.tvEdit.setOnClickListener {
            setEditingMode(true)
            adapter.setViewHolderType(LessonViewHolderTypes.REMOVING)
            adapter.submitList(viewModel.lessonList.toList())


        }
        binding.tvShow.setOnClickListener {
            setEditingMode(false)
            adapter.setViewHolderType(LessonViewHolderTypes.LISTING)
            adapter.submitList(viewModel.lessonList.toList())

        }
    }

    private fun setEditingMode(isEditing: Boolean) {
        binding.tvEdit.isVisible = !isEditing
        binding.tvShow.isVisible = isEditing
    }

}