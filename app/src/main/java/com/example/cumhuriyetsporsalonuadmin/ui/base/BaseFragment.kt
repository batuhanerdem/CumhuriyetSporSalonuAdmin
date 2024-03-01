package com.example.cumhuriyetsporsalonuadmin.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.viewbinding.ViewBinding
import com.example.cumhuriyetsporsalonuadmin.R
import com.example.cumhuriyetsporsalonuadmin.utils.MessageHandler
import com.example.cumhuriyetsporsalonuadmin.utils.ProgressBar
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy.Companion.stringfy
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

abstract class BaseFragment<ActionBus : BaseActionBus, ViewModel : BaseViewModel<ActionBus>, Binding : ViewBinding>(
    private val inflate: Inflate<Binding>, viewModelClass: Class<ViewModel>
) : Fragment() {

    val TAG = "tag"

    private var _binding: Binding? = null
    val binding get() = _binding!!

    val progressBar: ProgressBar by lazy {
        ProgressBar(requireActivity())
    }

    protected val viewModel: ViewModel by lazy {
        ViewModelProvider(this)[viewModelClass]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectActions()
        collectMessages()
        collectLoading()

        initPage()
        viewModel.init()
    }

    private fun collectActions() {
        viewModel.actionBus.onEach(::onAction).launchIn(viewLifecycleOwner.lifecycleScope)
    }

    abstract suspend fun onAction(action: ActionBus)

    private fun collectLoading() {
        viewModel.loadingState.onEach {
            if (it) progressBar.show()
            else progressBar.hide()
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    @Inject
    lateinit var messageHandler: MessageHandler

    private fun collectMessages() {
        viewModel.messageBus.onEach(::onMessage).launchIn(viewLifecycleOwner.lifecycleScope)
    }

    suspend fun onMessage(message: Stringfy) {
        showErrorMessage(message)
    }

    fun showSuccessMessage(message: Stringfy? = null) {
        progressBar.hide()
        val _message =
            message?.getString(requireContext()) ?: R.string.default_success_message.stringfy()
                .getString(requireContext())

        messageHandler.sneakerApiMessage(MessageHandler.StateRequest.Success, _message)
    }

    fun showErrorMessage(message: Stringfy? = null) {
        progressBar.hide()
        val _message =
            message?.getString(requireContext()) ?: R.string.default_error_message.stringfy()
                .getString(requireContext())
        messageHandler.sneakerApiMessage(MessageHandler.StateRequest.Error, _message)
    }

    abstract fun initPage()

    protected fun navigateTo(
        action: NavDirections, popUpTo: Int? = null, inclusive: Boolean = false
    ) {
        val options = popUpTo?.let {
            navOptions {
                popUpTo(popUpTo) {
                    this.inclusive = inclusive
                }
            }
        }
        findNavController().navigate(action, options)
    }

    protected fun navigateBack(popUpTo: Int? = null, inclusive: Boolean = false) {
        popUpTo?.let {
            findNavController().popBackStack(popUpTo, inclusive)
        } ?: run {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T