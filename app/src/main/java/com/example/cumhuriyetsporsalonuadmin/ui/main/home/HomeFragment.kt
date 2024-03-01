package com.example.cumhuriyetsporsalonuadmin.ui.main.home

import com.example.cumhuriyetsporsalonuadmin.databinding.FragmentHomeBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.User
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseFragment
import com.example.cumhuriyetsporsalonuadmin.ui.main.home.adapter.RequestAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeActionBus, HomeVIewModel, FragmentHomeBinding>(
    FragmentHomeBinding::inflate,
    HomeVIewModel::class.java
) {
    private lateinit var adapter: RequestAdapter
    override suspend fun onAction(action: HomeActionBus) {
        when (action) {
            HomeActionBus.Init -> {}
            is HomeActionBus.Success -> {
                adapter.submitList(action.list.toList())
            }

            HomeActionBus.Accepted -> viewModel.getUnverifiedUsers()
        }
    }

    override fun initPage() {
        viewModel.getUnverifiedUsers()
        setRV()
    }

    private fun setRV() {
        adapter = RequestAdapter(::answerRequest)
        binding.recyclerView.adapter = adapter
    }

    private fun answerRequest(user: User, isAccepted: Boolean) {
        viewModel.answerRequest(user, isAccepted)
    }


}