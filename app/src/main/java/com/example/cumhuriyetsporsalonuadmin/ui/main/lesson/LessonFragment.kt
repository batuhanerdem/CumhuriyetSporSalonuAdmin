package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson

import androidx.core.view.isVisible
import com.example.cumhuriyetsporsalonuadmin.R
import com.example.cumhuriyetsporsalonuadmin.databinding.FragmentLessonBinding
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseFragment
import com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.adapter.LessonAdapter
import com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.adapter.LessonRemovingAdapter
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy.Companion.stringfy
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LessonFragment : BaseFragment<LessonActionBus, LessonViewModel, FragmentLessonBinding>(
    FragmentLessonBinding::inflate, LessonViewModel::class.java
) {
    private lateinit var lessonAdapter: LessonAdapter
    private lateinit var lessonRemovingAdapter: LessonRemovingAdapter
    override suspend fun onAction(action: LessonActionBus) {
        when (action) {
            LessonActionBus.Init -> {}
            is LessonActionBus.ShowError -> {
                showErrorMessage(action.error)
            }

            is LessonActionBus.ClassesLoaded -> {
                lessonAdapter.submitList(viewModel.lessonList)
                setNoLessonFoundVisibility(viewModel.lessonList.isEmpty())
            }

            LessonActionBus.LessonDeleted -> {
                showSuccessMessage(
                    ("${viewModel.deletedLessonName} ${
                        R.string.lesson_removed.stringfy().getString(requireContext())
                    }").stringfy()
                )
                submitLists()
            }
        }
    }

    override fun initPage() {
        viewModel.getClasses()
        setupRV()
        setOnClickListeners()
    }

    private fun setupRV() {
        lessonAdapter = LessonAdapter {
            val action =
                LessonFragmentDirections.actionLessonFragmentToStudentListingByLessonFragment(it.uid)
            navigateTo(action)
        }
        lessonRemovingAdapter = LessonRemovingAdapter {
            viewModel.deleteLesson(it)
        }
        binding.rvLesson.adapter = lessonAdapter
    }

    private fun setOnClickListeners() {
        binding.apply {
            imgAdd.setOnClickListener {
                val action = LessonFragmentDirections.actionLessonFragmentToAddLessonFragment()
                navigateTo(action)
            }
            tvEdit.setOnClickListener {
                setEditingMode(true)
            }
            tvShow.setOnClickListener {
                setEditingMode(false)
            }
        }
    }

    private fun setNoLessonFoundVisibility(isEmpty: Boolean) {
        binding.noLessonFound.isVisible = isEmpty
        binding.rvLesson.isVisible = !isEmpty
    }

    private fun setEditingMode(isEditing: Boolean) {
        binding.apply {
            tvEdit.isVisible = !isEditing
            tvShow.isVisible = isEditing
            rvLesson.adapter = if (isEditing) lessonRemovingAdapter else lessonAdapter
        }
        submitLists()
    }

    private fun submitLists() {
        lessonAdapter.submitList(viewModel.lessonList.toList())
        lessonRemovingAdapter.submitList(viewModel.lessonList.toList())
    }
}