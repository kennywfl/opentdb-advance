package com.opentrivia.app.framework.view


interface QuickQuizView : BaseView {

    fun onTimeCountDown(remainingTime: Long)

    fun onCountDownFinished()
}