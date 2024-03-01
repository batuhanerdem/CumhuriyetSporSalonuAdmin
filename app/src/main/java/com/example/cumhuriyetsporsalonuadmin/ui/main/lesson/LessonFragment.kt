package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson

import com.example.cumhuriyetsporsalonuadmin.databinding.FragmentLessonBinding
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseFragment
import com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.adapter.LessonAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LessonFragment : BaseFragment<LessonActionBus, LessonViewModel, FragmentLessonBinding>(
    FragmentLessonBinding::inflate, LessonViewModel::class.java
) {
    private lateinit var adapter: LessonAdapter
    override suspend fun onAction(action: LessonActionBus) {
        when (action) {
            LessonActionBus.Init -> {}
            is LessonActionBus.ShowError -> {}
            is LessonActionBus.ClassesLoaded -> {
                adapter.submitList(action.lessonList.toList())
            }

            LessonActionBus.Loading -> {}
        }
    }

    override fun initPage() {
        viewModel.getClasses()
        setupRV()
        setOnClickListeners()
    }

    private fun setupRV() {
        adapter = LessonAdapter()
        binding.rvLesson.adapter = adapter
    }

    private fun setOnClickListeners() {
        binding.apply {
            btnAdd.setOnClickListener {
                val action = LessonFragmentDirections.actionLessonFragmentToAddLessonFragment()
                navigateTo(action)
            }
        }
    }

}