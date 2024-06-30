package com.example.cumhuriyetsporsalonuadmin.ui.auth.login

import android.content.Intent
import com.example.cumhuriyetsporsalonuadmin.R
import com.example.cumhuriyetsporsalonuadmin.databinding.FragmentLoginBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.Admin
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseFragment
import com.example.cumhuriyetsporsalonuadmin.ui.main.MainActivity
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
            LoginActionBus.Loading -> progressBar.show()
            LoginActionBus.LoggedIn -> {
                progressBar.hide()
                val message = R.string.login_success.stringfy()
//                showSuccessMessage(message)
                navigateHome()
            }

            is LoginActionBus.ShowError -> {
                progressBar.hide()
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
                val username = edtUsername.text.toString()
                val password = edtPassword.text.toString()
                if (username.isEmpty() || password.isEmpty()) return@setOnClickListener
                val admin = Admin(username, password)
                viewModel.loginWithEmailAndPassword(admin)
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