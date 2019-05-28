package com.sample.test.framework.view


interface QuickQuizView : BaseView {

    fun onTimeCountDown(remainingTime: Long)

    fun onCountDownFinished()
}