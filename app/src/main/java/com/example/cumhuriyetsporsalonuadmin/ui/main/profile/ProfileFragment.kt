package com.example.cumhuriyetsporsalonuadmin.ui.main.profile

import com.example.cumhuriyetsporsalonuadmin.R
import com.example.cumhuriyetsporsalonuadmin.databinding.FragmentProfileBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Admin
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseFragment
import com.example.cumhuriyetsporsalonuadmin.utils.NullOrEmptyValidator
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy.Companion.stringfy
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment : BaseFragment<ProfileActionBus, ProfileViewModel, FragmentProfileBinding>(
    FragmentProfileBinding::inflate, ProfileViewModel::class.java
) {
    override suspend fun onAction(action: ProfileActionBus) {
        when (action) {
            ProfileActionBus.Init -> {}
            ProfileActionBus.UpdatedSuccessFully -> {
                showSuccessMessage(R.string.update_success.stringfy())
                clearFields()
            }

            is ProfileActionBus.ShowError -> {
                val message = action.error ?: R.string.update_error_default.stringfy()
                showErrorMessage(message)
            }
        }
    }

    override fun initPage() {
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.btnSave.setOnClickListener {
            binding.apply {
                val isValidated = NullOrEmptyValidator.validate(edtUserName.text, edtPassword.text)
                if (!isValidated) return@setOnClickListener
                val username = binding.edtUserName.text.toString()
                val password = binding.edtPassword.text.toString()
                val admin = Admin(username, password)
                viewModel.updateAdmin(admin)
            }
        }
    }

    private fun clearFields() {
        binding.edtPassword.text.clear()
        binding.edtUserName.text.clear()
    }
}