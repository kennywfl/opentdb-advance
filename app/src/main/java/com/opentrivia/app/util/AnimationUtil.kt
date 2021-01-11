package com.opentrivia.app.util

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.opentrivia.app.R
import timber.log.Timber


object TransitionUtil {
    fun fadeIn(context: Context, targetView: View) {
        animateView(context, targetView, R.anim.fade_in, View.VISIBLE)
    }

    fun fadeOut(context: Context, targetView: View, retainView: Boolean = false) {
        if (retainView) {
            animateView(context, targetView, R.anim.fade_out, View.INVISIBLE)
        } else {
            animateView(context, targetView, R.anim.fade_out, View.GONE)
        }
    }

    fun slideInBottom(context: Context, targetView: View) {
        animateView(context, targetView, R.anim.slide_in_bottom, View.VISIBLE)
    }

    fun slideOutBottom(context: Context, targetView: View, retainView: Boolean = false) {
        animateView(context, targetView, R.anim.slide_out_bottom, if (retainView) View.INVISIBLE else View.GONE)
    }

    private fun animateView(context: Context, targetView: View?, anim: Int, visibility: Int) {
        try {
            val animate: Animation = AnimationUtils.loadAnimation(context, anim)
            animate.fillAfter = false
            targetView?.startAnimation(animate)
            targetView?.visibility = visibility

        } catch (e: Exception) {
            Timber.i(e)

            if (targetView != null) {
                targetView.visibility = visibility
            }
        }
    }
}