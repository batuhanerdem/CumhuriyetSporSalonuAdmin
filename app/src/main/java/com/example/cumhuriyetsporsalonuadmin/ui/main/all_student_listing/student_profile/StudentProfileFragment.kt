package com.example.cumhuriyetsporsalonuadmin.ui.main.all_student_listing.student_profile

import androidx.navigation.fragment.navArgs
import com.example.cumhuriyetsporsalonuadmin.databinding.FragmentStudentProfileBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudentProfileFragment :
    BaseFragment<StudentProfileActionBus, StudentProfileViewModel, FragmentStudentProfileBinding>(
        FragmentStudentProfileBinding::inflate, StudentProfileViewModel::class.java,
    ) {
    private val args: StudentProfileFragmentArgs by navArgs()

    override fun initPage() {
        viewModel.getStudent(args.uid)
        setOnClickListeners()
    }

    override suspend fun onAction(action: StudentProfileActionBus) {
        when (action) {
            is StudentProfileActionBus.ShowError -> {

            }

            StudentProfileActionBus.Init -> {}
            StudentProfileActionBus.StudentsLoaded -> {
                viewModel.user?.let {
                    setUserProfile(it)
                }
            }
        }
    }

    private fun setUserProfile(user: Student) {
        binding.apply {
            "${user.name} ${user.surname}".also { tvName.text = it }
            tvHeightShow.text = user.height ?: "-"
            tvWeightShow.text = user.weight ?: "-"
            tvAgeShow.text = user.age ?: "-"
            tvBMIShow.text = user.bmi ?: "-"
        }
    }

    private fun setOnClickListeners() {
        binding.tvEditProfile.setOnClickListener {
            val action =
                StudentProfileFragmentDirections.actionStudentProfileFragmentToEditStudentProfileFragment(
                    args.uid
                )
            navigateTo(action)
        }
        binding.layoutLessons.setOnClickListener {
            val action =
                StudentProfileFragmentDirections.actionStudentProfileFragmentToLessonListingByStudent(
                    args.uid
                )
            navigateTo(action)
        }
        binding.imgBack.setOnClickListener {
            navigateBack()
        }

    }


}