package com.opentrivia.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.valueIterator
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.opentrivia.advance.R
import com.opentrivia.advance.databinding.FragmentQuizBinding
import com.opentrivia.app.adapter.SpinnerAdapter
import com.opentrivia.app.extension.hide
import com.opentrivia.app.extension.show
import com.opentrivia.app.framework.model.QuizViewModel
import com.opentrivia.app.framework.presenter.QuizPresenter
import com.opentrivia.app.framework.view.QuizView
import com.opentrivia.app.lib.datasource.model.Questions
import javax.inject.Inject


class QuizFragment : BaseFragment(), QuizView {

    @Inject
    lateinit var presenter: QuizPresenter
    private lateinit var categoryAdapter: SpinnerAdapter
    private lateinit var quizViewModel: QuizViewModel
    private lateinit var binding: FragmentQuizBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizViewModel = activity?.run {
            ViewModelProviders.of(this).get(QuizViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        quizViewModel.answerMap.observe(this) { it ->
            val correctQuestion = it.valueIterator().asSequence().count {
                it
            }
            binding.cvResult.show()
            binding.tvTimeTaken.text =
                getString(R.string.remaining_time, quizViewModel.remainingTime)
            binding.tvCorrectCount.text =
                getString(
                    R.string.correct_answer_count,
                    correctQuestion,
                    quizViewModel.questionList.size
                )
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOptionSpinners()
        binding.btnNext.setOnClickListener {
            showLoadingProgress()
            enableInput(false)
            quizViewModel.clear()
            binding.cvResult.hide()
            presenter.getQuestionForQuickQuiz(binding.spCategory.selectedView.tag as String)
        }
        binding.cvResult.setOnClickListener {
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

    private fun initCategorySpinner() {
        val list = appSp.retrieveCategories()
        val defaultValue = "Default" to getString(R.string.noop)
        list.add(0, defaultValue)
        context?.let {
            categoryAdapter = SpinnerAdapter(it, list)
            binding.spCategory.adapter = categoryAdapter
        }
    }

    private fun setupOptionSpinners() {
        initCategorySpinner()
    }

    private fun enableInput(enable: Boolean) {
        binding.spCategory.isEnabled = enable
        binding.btnNext.isEnabled = enable
    }

    private fun showLoadingProgress() {
        binding.pbLoading.show()
    }

    private fun hideLoadingProgress() {
        binding.pbLoading.hide()
    }
}