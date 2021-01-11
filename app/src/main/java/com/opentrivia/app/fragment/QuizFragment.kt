package com.opentrivia.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.valueIterator
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.opentrivia.app.R
import com.opentrivia.app.adapter.SpinnerAdapter
import com.opentrivia.app.extension.hide
import com.opentrivia.app.extension.show
import com.opentrivia.app.framework.model.QuizViewModel
import com.opentrivia.app.framework.presenter.QuizPresenter
import com.opentrivia.app.framework.view.QuizView
import com.opentrivia.app.lib.datasource.model.Questions
import kotlinx.android.synthetic.main.fragment_quiz.*
import javax.inject.Inject


class QuizFragment : BaseFragment(), QuizView {

    @Inject
    lateinit var presenter: QuizPresenter
    private lateinit var categoryAdapter: SpinnerAdapter
    private lateinit var quizViewModel: QuizViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizViewModel = activity?.run {
            ViewModelProviders.of(this).get(QuizViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        quizViewModel.answerMap.observe(this, Observer {
            val correctQuestion = it.valueIterator().asSequence().count {
                it == true
            }
            cv_result.show()
            tv_time_taken.text = getString(R.string.remaining_time, quizViewModel.remainingTime)
            tv_correct_count.text =
                getString(
                    R.string.correct_answer_count,
                    correctQuestion,
                    quizViewModel.questionList.size
                )
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOptionSpinners()
        btn_next.setOnClickListener {
            showLoadingProgress()
            enableInput(false)
            quizViewModel.clear()
            cv_result.hide()
            presenter.getQuestionForQuickQuiz(sp_category.selectedView.tag as String)
        }
        cv_result.setOnClickListener {
            findNavController().navigate(R.id.action_quiz_fragment_to_result_dialog)
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.bindView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.unbindView()
    }

    override fun onRetrieveQuestionList(questions: MutableList<Questions>) {
        hideLoadingProgress()
        quizViewModel.questionList = questions
        enableInput(true)
        findNavController().navigate(R.id.action_quiz_fragment_to_quick_quiz_dialog)
    }

    fun initCategorySpinner() {
        val list = appSp.retrieveCategories()
        val defaultValue = "Default" to getString(R.string.noop)
        list.add(0, defaultValue)
        context?.let {
            categoryAdapter = SpinnerAdapter(it, list)
            sp_category.adapter = categoryAdapter
        }
    }

    fun setupOptionSpinners() {
        initCategorySpinner()
    }

    fun enableInput(enable: Boolean) {
        sp_category.isEnabled = enable
        btn_next.isEnabled = enable
    }

    fun showLoadingProgress() {
        pb_loading.show()
    }

    fun hideLoadingProgress() {
        pb_loading.hide()
    }
}