package com.example.cumhuriyetsporsalonuadmin.utils

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.animation.Animation
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.RawRes
import com.airbnb.lottie.LottieAnimationView
import com.example.cumhuriyetsporsalonuadmin.R

class CustomProgressBar() {

    private lateinit var mainDialog: Dialog

    private var mainDialogLayout: RelativeLayout? = null

    private var mainialogAnimationContainer: LinearLayout? = null

    private lateinit var mainDialogAnimationView: LottieAnimationView

    private var isAutoPlayedAnimation = false

    private val mainLottieDialogLayout: Int = R.layout.layout_custom_progress_bar

    private val linearLayoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1F
    )

    constructor(context: Context) : this() {
        val view = LayoutInflater.from(context).inflate(mainLottieDialogLayout, null)
        mainDialog = Dialog(context)
        mainDialog.setContentView(view)
        mainDialogAnimationView = view.findViewById(R.id.lottie_dialog_animation)
        mainDialogLayout = view.findViewById(R.id.lottie_dialog_layout)
        mainialogAnimationContainer = view.findViewById(R.id.lottie_dialog_animation_container)
        mainDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }

    fun setDialogDimAmount(amount: Float): CustomProgressBar {
        mainDialog.window!!.setDimAmount(amount)
        return this
    }

    fun setCancelable(cancelable: Boolean): CustomProgressBar {
        mainDialog.setCancelable(cancelable)
        return this
    }

    fun setCanceledOnTouchOutside(cancel: Boolean): CustomProgressBar {
        mainDialog.setCanceledOnTouchOutside(cancel)
        return this
    }

    fun setDialogHeight(height: Int): CustomProgressBar {
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(mainDialog.window!!.attributes)
        layoutParams.height = height
        mainDialog.window!!.attributes = layoutParams
        return this
    }

    fun setDialogWidth(width: Int): CustomProgressBar {
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(mainDialog.window!!.attributes)
        layoutParams.width = width
        mainDialog.window!!.attributes = layoutParams
        return this
    }

    fun setDialogHeightPercentage(percentage: Float): CustomProgressBar {
        val metrics = Resources.getSystem().displayMetrics
        val heightPixels = (metrics.heightPixels * percentage).toInt()
        return setDialogHeight(heightPixels)
    }

    fun setDialogWidthPercentage(percentage: Float): CustomProgressBar {
        val metrics = Resources.getSystem().displayMetrics
        val widthPixels = (metrics.widthPixels * percentage).toInt()
        return setDialogWidth(widthPixels)
    }


    fun setAnimationViewHeight(height: Int): CustomProgressBar {
        val layoutParams = mainialogAnimationContainer!!.layoutParams
        layoutParams.height = height
        mainialogAnimationContainer?.layoutParams = layoutParams
        return this
    }


    fun setAnimationViewWidth(width: Int): CustomProgressBar {
        val layoutParams = mainialogAnimationContainer!!.layoutParams
        layoutParams.width = width
        mainialogAnimationContainer?.layoutParams = layoutParams
        return this
    }


    fun setAnimation(@RawRes rawRes: Int): CustomProgressBar {
        mainDialogAnimationView.setAnimation(rawRes)
        return this
    }


    fun setAnimation(animation: Animation?): CustomProgressBar {
        mainDialogAnimationView.animation = animation
        return this
    }


    fun setAnimation(assetName: String?): CustomProgressBar {
        mainDialogAnimationView.setAnimation(assetName)
        return this
    }


    fun setAnimationRepeatCount(count: Int): CustomProgressBar {
        mainDialogAnimationView.repeatCount = count
        return this
    }

    fun setAnimationSpeed(speed: Float): CustomProgressBar {
        mainDialogAnimationView.speed = speed
        return this
    }

    fun setAutoPlayAnimation(autoplay: Boolean): CustomProgressBar {
        isAutoPlayedAnimation = autoplay
        return this
    }

    fun isAutoPlayedAnimation(): Boolean {
        return isAutoPlayedAnimation
    }

    fun playAnimation() {
        mainDialogAnimationView.playAnimation()
    }

    fun pauseAnimation() {
        mainDialogAnimationView.pauseAnimation()
    }

    fun cancelAnimation() {
        mainDialogAnimationView.cancelAnimation()
    }

    fun clearAnimation() {
        mainDialogAnimationView.clearAnimation()
    }

    fun reverseAnimationSpeed() {
        mainDialogAnimationView.reverseAnimationSpeed()
    }

    fun isAnimating(): Boolean {
        return mainDialogAnimationView.isAnimating
    }

    fun show() {
        mainDialog.show()
        if (isAutoPlayedAnimation) playAnimation()
    }

    fun isShowing(): Boolean {
        return mainDialog.isShowing
    }

    fun dismiss() {
        mainDialog.dismiss()
    }

    enum class RepeatCount(val count: Int) {
        INFINITE(999)
    }
}