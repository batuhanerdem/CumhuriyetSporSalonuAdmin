package com.example.cumhuriyetsporsalonuadmin.ui.main.home.verify_request

import androidx.core.view.isVisible
import com.example.cumhuriyetsporsalonuadmin.databinding.FragmentVerifyRequestBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.User
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseFragment
import com.example.cumhuriyetsporsalonuadmin.ui.main.home.verify_request.adapter.VerifyRequestAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyRequestFragment :
    BaseFragment<VerifyRequestActionBus, VerifyRequestViewModel, FragmentVerifyRequestBinding>(
        FragmentVerifyRequestBinding::inflate, VerifyRequestViewModel::class.java
    ) {
    private lateinit var adapter: VerifyRequestAdapter
    override suspend fun onAction(action: VerifyRequestActionBus) {
        when (action) {
            VerifyRequestActionBus.Init -> {}
            is VerifyRequestActionBus.ApplicationsLoaded -> {
                adapter.submitList(viewModel.unverifiedList)
                setNoFoundVisibility(viewModel.unverifiedList.isEmpty())
            }

            VerifyRequestActionBus.Accepted -> viewModel.getUnverifiedUsers()
            is VerifyRequestActionBus.ShowError -> {
                showErrorMessage(action.error)
            }
        }
    }

    override fun initPage() {
        viewModel.getUnverifiedUsers()
        setRV()
    }

    private fun setRV() {
        adapter = VerifyRequestAdapter(::answerRequest)
        binding.rvApplication.adapter = adapter
    }

    private fun setNoFoundVisibility(isEmpty: Boolean) {
        binding.noApplicationFound.isVisible = isEmpty
        binding.rvApplication.isVisible = !isEmpty
    }

    private fun answerRequest(user: User, isAccepted: Boolean) {
        viewModel.answerRequest(user, isAccepted)
    }

}