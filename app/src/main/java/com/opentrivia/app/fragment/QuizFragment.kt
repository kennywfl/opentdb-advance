package com.opentrivia.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.core.util.valueIterator
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.opentrivia.advance.R
import com.opentrivia.app.framework.model.QuizViewModel
import com.opentrivia.app.framework.presenter.QuizPresenter
import com.opentrivia.app.framework.view.QuizView
import com.opentrivia.app.lib.datasource.model.Questions
import com.opentrivia.app.ui.screen.QuizScreen
import com.opentrivia.app.ui.theme.AppTheme
import javax.inject.Inject


class QuizFragment : BaseFragment(), QuizView {

    @Inject
    lateinit var presenter: QuizPresenter
    private lateinit var quizViewModel: QuizViewModel

    private var isInputEnabled by mutableStateOf(true)
    private var isLoading by mutableStateOf(false)
    private var isResultVisible by mutableStateOf(false)
    private var resultTime by mutableStateOf("")
    private var resultCorrectCount by mutableStateOf("")
    private var categories by mutableStateOf<List<Pair<String, String>>>(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizViewModel = activity?.run {
            ViewModelProviders.of(this).get(QuizViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        quizViewModel.answerMap.observe(this) { it ->
            val correctQuestion = it.valueIterator().asSequence().count { it }
            isResultVisible = true
            isLoading = false
            resultTime = getString(R.string.remaining_time, quizViewModel.remainingTime)
            resultCorrectCount = getString(
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
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    QuizScreen(
                        categories = categories,
                        isInputEnabled = isInputEnabled,
                        isLoading = isLoading,
                        isResultVisible = isResultVisible,
                        resultTime = resultTime,
                        resultCorrectCount = resultCorrectCount,
                        onCategorySelected = { index ->
                            val selected = categories[index]
                        },
                        onNextClick = {
                            isLoading = true
                            isInputEnabled = false
                            quizViewModel.clear()
                            isResultVisible = false
                            val list = appSp.retrieveCategories()
                            val categoryId = list.firstOrNull()?.second ?: ""
                            presenter.getQuestionForQuickQuiz(categoryId)
                        },
                        onResultClick = {
                            findNavController().navigate(R.id.action_quiz_fragment_to_result_dialog)
                        }
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categories = appSp.retrieveCategories().toMutableList().apply {
            add(0, "Default" to "noop")
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
        isLoading = false
        quizViewModel.questionList = questions
        isInputEnabled = true
        findNavController().navigate(R.id.action_quiz_fragment_to_quick_quiz_dialog)
    }
}
