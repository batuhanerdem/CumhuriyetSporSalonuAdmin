package com.example.cumhuriyetsporsalonuadmin.ui.main.all_student_listing.student_profile.edit_profile

import androidx.navigation.fragment.navArgs
import com.example.cumhuriyetsporsalonuadmin.R
import com.example.cumhuriyetsporsalonuadmin.databinding.FragmentEditStudentProfileBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseFragment
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy.Companion.stringfy
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditStudentProfileFragment :
    BaseFragment<EditStudentProfileActionBus, EditStudentProfileViewModel, FragmentEditStudentProfileBinding>(
        FragmentEditStudentProfileBinding::inflate, EditStudentProfileViewModel::class.java,
    ) {
    private val args: EditStudentProfileFragmentArgs by navArgs()

    override fun initPage() {
        setOnClickListeners()
        viewModel.getStudent(args.uid)
    }

    override suspend fun onAction(action: EditStudentProfileActionBus) {
        when (action) {
            is EditStudentProfileActionBus.ShowError -> {}
            EditStudentProfileActionBus.Init -> {}
            EditStudentProfileActionBus.UserUpdated -> {
                showSuccessMessage(R.string.student_saved.stringfy())
                clearFields()
            }

            EditStudentProfileActionBus.StudentLoaded -> {
                viewModel.student?.let {
                    setEdittext(it)
                }
            }
        }
    }

    private fun setEdittext(user: Student) {
        binding.apply {
            edtName.setText(user.name)
            edtSurname.setText(user.surname)
            edtAge.setText(user.age)
        }
    }

    private fun setOnClickListeners() {
        binding.imgBack.setOnClickListener() {
            navigateBack()
        }
        binding.btnSave.setOnClickListener() {
            val name = binding.edtName.text.toString()
            val surname = binding.edtSurname.text.toString()
            val age = binding.edtAge.text.toString()
            viewModel.saveUser(name, surname, age)
        }
    }

    private fun clearFields() {
        binding.apply {
            edtName.text.clear()
            edtSurname.text.clear()
            edtName.text.clear()
        }
        viewModel.student?.let {
            setEdittext(it)
        }
    }


}