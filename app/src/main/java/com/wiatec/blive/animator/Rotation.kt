package com.wiatec.blive.animator

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

/**
 * Created by patrick on 19/10/2017.
 * create time : 5:12 PM
 */

object Rotation {

    fun r180(view: View) {
        val animator = ObjectAnimator.ofFloat(view, "rotationY", 0f, 180f)
        val animatorSet = AnimatorSet()
        animatorSet.duration = 300
        animatorSet.play(animator)
        animatorSet.start()
    }
}
