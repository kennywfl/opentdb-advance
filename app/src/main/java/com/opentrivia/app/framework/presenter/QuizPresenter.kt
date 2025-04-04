package com.opentrivia.app.framework.presenter

import com.opentrivia.app.framework.view.QuizView
import com.opentrivia.app.lib.Constants
import com.opentrivia.app.lib.datasource.DataManager
import com.opentrivia.app.lib.datasource.model.Questions
import com.opentrivia.app.lib.extension.apiSubscribeBy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject


class QuizPresenter @Inject constructor(
    private val dataManager: DataManager,
    view: QuizView
) : BasePresenter<QuizView>() {

    init {
        bindView(view)
    }

    fun getQuestionForQuickQuiz(
        category: String? = null
    ) {
        val realCategory = category?.run {
            if (equals("noop")) {
                null
            } else {
                toInt()
            }
        }
        dataManager.getTriviaWithToken(
            amount = Constants.QUIZ_SIZE,
            category = realCategory
        )
            .map { it ->
                val questions = mutableListOf<Questions>()
                if (it.results.isNotEmpty()) {
                    it.results.forEach { result ->
                        val listOfAnswer = mutableListOf(result.correctAnswer to true)
                        result.incorrectAnswers.forEach {
                            listOfAnswer.add(it to false)
                        }
                        listOfAnswer.shuffle()
                        questions.add(
                            Questions(
                                result.category,
                                result.question,
                                result.difficulty,
                                Constants.Api.PARAM_MULTIPLE == result.type,
                                listOfAnswer
                            )
                        )
                    }
                }
                questions
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .apiSubscribeBy({
                subscription += it
            }, {
                getView()?.onRetrieveQuestionList(it)
            }, {
                Timber.e(it)
                getView()?.onError(it.displayMessage)
            })
    }
}