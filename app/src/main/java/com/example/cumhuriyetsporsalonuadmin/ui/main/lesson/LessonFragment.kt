package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
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
            is LessonActionBus.ShowError -> {
                progressBar.hide()
            }

            is LessonActionBus.ClassesLoaded -> {
                adapter.submitList(viewModel.lessonList)
                Log.d(TAG, "onAction: ${viewModel.lessonList.count()}")
                progressBar.hide()
            }

            LessonActionBus.Loading -> progressBar.show()
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
//            btnAdd.setOnClickListener {
//                val action = LessonFragmentDirections.actionLessonFragmentToAddLessonFragment()
//                navigateTo(action)
//            }
        }
    }

}