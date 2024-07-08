package com.example.cumhuriyetsporsalonuadmin.ui.auth.login

import android.content.Intent
import com.example.cumhuriyetsporsalonuadmin.R
import com.example.cumhuriyetsporsalonuadmin.databinding.FragmentLoginBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Admin
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseFragment
import com.example.cumhuriyetsporsalonuadmin.ui.main.MainActivity
import com.example.cumhuriyetsporsalonuadmin.utils.NullOrEmptyValidator
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy.Companion.stringfy
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginActionBus, LoginViewModel, FragmentLoginBinding>(
    FragmentLoginBinding::inflate, LoginViewModel::class.java,
) {

    override fun initPage() {
        setupOnClickListeners()

    }

    override suspend fun onAction(action: LoginActionBus) {
        when (action) {
            LoginActionBus.Init -> {}
            LoginActionBus.LoggedIn -> {
                navigateHome()
            }

            is LoginActionBus.ShowError -> {
                val message = action.error ?: R.string.login_error_default.stringfy()
                showErrorMessage(message)
            }
        }
    }

    private fun setupOnClickListeners() {
        binding.apply {
            btnLogin.setOnClickListener {
                viewModel.loginForTest()
                return@setOnClickListener
                val isValidated = NullOrEmptyValidator.validate(edtUsername.text, edtPassword.text)
                if (!isValidated) return@setOnClickListener
                val username = edtUsername.text.toString()
                val password = edtPassword.text.toString()
                viewModel.login(Admin(username, password))
            }
        }
    }

    private fun navigateHome() {
        Intent(requireContext(), MainActivity::class.java).apply {
            startActivity(this)
            requireActivity().finish()
        }
    }
}