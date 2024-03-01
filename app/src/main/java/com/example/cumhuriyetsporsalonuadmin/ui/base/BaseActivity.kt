package com.example.cumhuriyetsporsalonuadmin.ui.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class BaseActivity<ActionBus : BaseActionBus, ViewModel : BaseViewModel<ActionBus>, Binding : ViewBinding>(
    private val inflate: InflateActivity<Binding>, viewModelClass: Class<ViewModel>
) : AppCompatActivity() {
    val TAG = "tag" // For Testing

    private var _binding: Binding? = null
    val binding get() = _binding!!

    protected val viewModel: ViewModel by lazy {
        ViewModelProvider(this)[viewModelClass]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = inflate.invoke(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            val param = binding.root.layoutParams as ViewGroup.MarginLayoutParams
            param.setMargins(0, 0, 0, insets.bottom)
            binding.root.layoutParams = param

            WindowInsetsCompat.CONSUMED
        }
        init()

        with(viewModel) {
            actionBus.onEach(::onAction).launchIn(lifecycleScope)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun goTo(activity: Class<out AppCompatActivity>) {
        val intent = Intent(this, activity)
        startActivity(intent)
    }

    abstract fun init()

    abstract suspend fun onAction(action: ActionBus)

}

private typealias InflateActivity<T> = (LayoutInflater) -> T