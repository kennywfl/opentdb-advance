package com.sample.test.lib.logger

import timber.log.Timber


object TimberImpl {
    fun plant() {
        Timber.plant(Timber.DebugTree())
    }
}