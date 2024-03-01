package com.example.cumhuriyetsporsalonuadmin.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.viewbinding.ViewBinding
import com.example.cumhuriyetsporsalonuadmin.utils.MessageHandler
import com.example.cumhuriyetsporsalonuadmin.utils.ProgressBar
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseDialogFragment<ActionBus : BaseActionBus, ViewModel : BaseViewModel<ActionBus>, Binding : ViewBinding>(
    private val inflate: Inflate<Binding>,
    viewModelClass: Class<ViewModel>
) : DialogFragment() {
    val TAG = this::class.java.simpleName // For Testing

    private var _binding: Binding? = null
    val binding get() = _binding!!

    val progressBar: ProgressBar by lazy {
        ProgressBar(requireActivity())
    }

    protected val viewModel: ViewModel by lazy {
        ViewModelProvider(this)[viewModelClass]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadingState.collect {
                if (it) progressBar.show()
                else progressBar.hide()
            }
        }
    }

    @Inject
    lateinit var messageHandler: MessageHandler

    private fun collectMessages() {
        viewModel.messageBus.onEach(::onMessage).launchIn(viewLifecycleOwner.lifecycleScope)
    }

    suspend fun onMessage(message: Stringfy) {
        showErrorMessage(message.getString(requireContext()))
    }

    fun showSuccessMessage(message: String?) {
        messageHandler.toastApiMessage(MessageHandler.StateRequest.Success, message)
    }

    fun showErrorMessage(message: String?) {
        messageHandler.toastApiMessage(MessageHandler.StateRequest.Error, message)
    }

    abstract fun initPage()

    protected fun navigateTo(
        action: NavDirections,
        popUpTo: Int? = null,
        inclusive: Boolean = false
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