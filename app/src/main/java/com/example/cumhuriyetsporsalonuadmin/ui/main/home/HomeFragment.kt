package com.example.cumhuriyetsporsalonuadmin.ui.main.home

import com.example.cumhuriyetsporsalonuadmin.R
import com.example.cumhuriyetsporsalonuadmin.databinding.FragmentHomeBinding
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseFragment
import com.example.cumhuriyetsporsalonuadmin.ui.main.home.adapter.FragmentAdapter
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy.Companion.stringfy
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeActionBus, HomeVIewModel, FragmentHomeBinding>(
    FragmentHomeBinding::inflate, HomeVIewModel::class.java
) {
    override suspend fun onAction(action: HomeActionBus) {
        when (action) {
            HomeActionBus.Init -> {}
            is HomeActionBus.ShowError -> showErrorMessage(action.error)

        }
    }

    override fun initPage() {
        setViewPager()
    }

    private fun setViewPager() {
        binding.viewPager.adapter = FragmentAdapter(childFragmentManager, lifecycle)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = R.string.sign_up_requests.stringfy().getString(requireContext())
                1 -> tab.text = R.string.lesson_requests.stringfy().getString(requireContext())
                else -> tab.text = ""
            }
        }.attach()
    }

}