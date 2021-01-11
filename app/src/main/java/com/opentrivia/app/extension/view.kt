package com.opentrivia.app.extension

import android.view.View
import com.opentrivia.app.util.TransitionUtil


/**
 * To show [View]
 */
fun View.show() {
    this.visibility = View.VISIBLE
}

/**
 * To hide  [View]
 */
fun View.hide() {
    this.visibility = View.GONE
}

/**
 * To show [View]
 */
fun View.fadeIn() {
    this.apply {
        translationY = 0.0f
        show()
        animate()
            .translationY(1.0f)
            .setDuration(400L)
            .start()
    }
}

/**
 * To hide  [View]
 */
fun View.fadeOut() {
    TransitionUtil.slideOutBottom(context, this)
}