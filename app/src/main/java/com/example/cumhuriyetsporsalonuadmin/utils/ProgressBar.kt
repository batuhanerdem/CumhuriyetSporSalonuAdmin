package com.example.cumhuriyetsporsalonuadmin.utils

import androidx.fragment.app.FragmentActivity
import com.example.cumhuriyetsporsalonuadmin.R
import com.example.cumhuriyetsporsalonuadmin.utils.CustomProgressBar

class ProgressBar(activity: FragmentActivity) {

    private val dialog: CustomProgressBar = CustomProgressBar(activity)

    init {
        dialog.setAnimation(R.raw.boxing_animation)
            .setAnimationRepeatCount(CustomProgressBar.RepeatCount.INFINITE.count)
            .setAutoPlayAnimation(true)
            .setCancelable(false)
            .setDialogHeightPercentage(0.15f)
            .setDialogWidthPercentage(0.25f)
    }

    fun show() {
        if (!dialog.isShowing()) dialog.show()
    }

    fun hide() {
        if (dialog.isShowing()) dialog.dismiss()
    }
}