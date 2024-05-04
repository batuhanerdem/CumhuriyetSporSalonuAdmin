package com.example.cumhuriyetsporsalonuadmin.ui.main.profile

import com.example.cumhuriyetsporsalonuadmin.R
import com.example.cumhuriyetsporsalonuadmin.databinding.FragmentProfileBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Admin
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseFragment
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy.Companion.stringfy
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment : BaseFragment<ProfileActionBus, ProfileViewModel, FragmentProfileBinding>(
    FragmentProfileBinding::inflate,
    ProfileViewModel::class.java
) {
    override suspend fun onAction(action: ProfileActionBus) {
        when (action) {
            ProfileActionBus.Init -> TODO()
            ProfileActionBus.UpdatedSuccessFully -> showSuccessMessage(R.string.update_success.stringfy())
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
//        binding.btnUpdate.setOnClickListener {
//            val username = binding.edtUsername.text.toString()
//            val password = binding.edtPassword.text.toString()
//            if (username.isEmpty() || password.isEmpty()) return@setOnClickListener
//            val admin = Admin(username, password)
//            viewModel.updateAdmin(admin)
//        }
    }

}