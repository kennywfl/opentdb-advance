package com.opentrivia.app.lib.logger

import timber.log.Timber


object TimberImpl {
    fun plant() {
        Timber.plant(ReleaseTree())
    }
}