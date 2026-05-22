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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.opentrivia.advance.R
import com.opentrivia.app.model.QuizViewModel
import com.opentrivia.app.lib.Constants
import com.opentrivia.app.lib.datasource.DataManager
import com.opentrivia.app.lib.datasource.model.Questions
import com.opentrivia.app.ui.screen.QuizScreen
import com.opentrivia.app.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class QuizFragment : BaseFragment() {

    private val dataManager: DataManager by inject()
    private val quizViewModel: QuizViewModel by viewModels(ownerProducer = { requireActivity() })

    private var isInputEnabled by mutableStateOf(true)
    private var isLoading by mutableStateOf(false)
    private var isResultVisible by mutableStateOf(false)
    private var resultTime by mutableStateOf("")
    private var resultCorrectCount by mutableStateOf("")
    private var categories by mutableStateOf<List<Pair<String, String>>>(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                        onCategorySelected = { },
                        onNextClick = {
                            isLoading = true
                            isInputEnabled = false
                            quizViewModel.clear()
                            isResultVisible = false
                            val list = appSp.retrieveCategories()
                            val categoryId = list.firstOrNull()?.second ?: ""
                            fetchQuestions(categoryId)
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

    private fun fetchQuestions(categoryId: String) {
        val realCategory = if (categoryId == "noop") null else categoryId.toIntOrNull()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = dataManager.getTriviaWithToken(
                    amount = Constants.QUIZ_SIZE,
                    category = realCategory
                )
                val questions = mutableListOf<Questions>()
                if (response.results.isNotEmpty()) {
                    response.results.forEach { result ->
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
                requireActivity().runOnUiThread {
                    isLoading = false
                    quizViewModel.questionList = questions
                    isInputEnabled = true
                    findNavController().navigate(R.id.action_quiz_fragment_to_quick_quiz_dialog)
                }
            } catch (e: Exception) {
                requireActivity().runOnUiThread {
                    isLoading = false
                    showError(e.message)
                }
            }
        }
    }
}
