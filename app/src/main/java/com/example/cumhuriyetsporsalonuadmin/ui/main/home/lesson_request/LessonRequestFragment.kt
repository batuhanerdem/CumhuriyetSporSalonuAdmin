package com.example.cumhuriyetsporsalonuadmin.ui.main.home.lesson_request

import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.cumhuriyetsporsalonuadmin.databinding.FragmentVerifyRequestBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.LessonRequest
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseFragment
import com.example.cumhuriyetsporsalonuadmin.ui.main.home.HomeFragmentDirections
import com.example.cumhuriyetsporsalonuadmin.ui.main.home.lesson_request.adapter.LessonRequestAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LessonRequestFragment :
    BaseFragment<LessonRequestActionBus, LessonRequestViewModel, FragmentVerifyRequestBinding>(
        FragmentVerifyRequestBinding::inflate, LessonRequestViewModel::class.java
    ) {
    private lateinit var adapter: LessonRequestAdapter
    override suspend fun onAction(action: LessonRequestActionBus) {
        when (action) {
            LessonRequestActionBus.Answered -> {
                setNoFoundVisibility(viewModel.requestList.isEmpty())
                adapter.submitList(viewModel.requestList.toList())
            }

            LessonRequestActionBus.ApplicationsLoaded -> {
                setNoFoundVisibility(viewModel.requestList.isEmpty())
                adapter.submitList(viewModel.requestList.toList())
            }

            LessonRequestActionBus.Init -> {}
            is LessonRequestActionBus.ShowError -> {
                showErrorMessage(action.error)
            }
        }
    }

    override fun initPage() {
//        viewModel.getUnverifiedUsers()
        setRV()
        viewModel.getRequest()
    }

    private fun setRV() {
        adapter = LessonRequestAdapter(::goToLesson, ::goToStudent, ::answerRequest)
        binding.rvApplication.adapter = adapter
    }

    private fun goToLesson(lesson: Lesson) {
        val action = HomeFragmentDirections.actionHomeFragmentToStudentListingByLessonFragment(
            lesson.uid
        )
        parentFragment?.findNavController()?.navigate(action)
    }

    private fun goToStudent(student: Student) {
        val action = HomeFragmentDirections.actionHomeFragmentToStudentProfileFragment(
            student.uid
        )
        parentFragment?.findNavController()?.navigate(action)

    }

    private fun setNoFoundVisibility(isEmpty: Boolean) {
        binding.noApplicationFound.isVisible = isEmpty
        binding.rvApplication.isVisible = !isEmpty
    }

    private fun answerRequest(lessonRequest: LessonRequest, isAccepted: Boolean) {
        viewModel.answerRequest(lessonRequest, isAccepted)
    }

}