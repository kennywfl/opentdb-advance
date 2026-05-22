package com.opentrivia.app.dialogfragment

import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.opentrivia.advance.R
import com.opentrivia.app.model.QuizViewModel
import com.opentrivia.app.lib.Constants
import com.opentrivia.app.ui.screen.QuickQuizScreen
import com.opentrivia.app.ui.theme.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope


class QuickQuizDialogFragment : BaseDialogFragment() {

    private val quizViewModel: QuizViewModel by viewModels(ownerProducer = { requireActivity() })

    private var count by mutableIntStateOf(1)
    private val answerMap = SparseBooleanArray()

    private var questionCountText by mutableStateOf("")
    private var timerText by mutableStateOf("01:00")
    private var timerColor by mutableStateOf(androidx.compose.ui.graphics.Color.Unspecified)
    private var questionText by mutableStateOf("")
    private var difficulty by mutableStateOf("")
    private var option1 by mutableStateOf("")
    private var option2 by mutableStateOf("")
    private var option3 by mutableStateOf<String?>(null)
    private var option4 by mutableStateOf<String?>(null)
    private var isMultiple by mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.NoTitleDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    QuickQuizScreen(
                        questionCount = questionCountText,
                        timerText = timerText,
                        timerColor = timerColor,
                        questionText = questionText,
                        difficulty = difficulty,
                        option1 = option1,
                        option2 = option2,
                        option3 = option3,
                        option4 = option4,
                        isMultiple = isMultiple,
                        onCloseClick = { dismiss() },
                        onOptionClick = { id ->
                            answerMap.put(count, id == 1 || id == 2 || id == 3 || id == 4)
                            if (count >= Constants.QUIZ_SIZE) {
                                quizViewModel.remainingTime = timerText
                                quizViewModel.answerMap.postValue(answerMap)
                                dismiss()
                            } else {
                                count++
                                populateQuestionContent()
                            }
                        }
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateQuestionContent()
        startTimer()
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        if (window != null) {
            val params = window.attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.MATCH_PARENT
            window.attributes = params as WindowManager.LayoutParams
        }
    }

    private fun startTimer() {
        lifecycleScope.launch {
            for (i in 1..Constants.COUNT_DOWN_TIMER) {
                delay(1000)
                val remaining = Constants.COUNT_DOWN_TIMER - i
                timerColor = if (remaining > 5) {
                    androidx.compose.ui.graphics.Color.Unspecified
                } else {
                    com.opentrivia.app.ui.theme.Red
                }
                timerText = Constants.COUNT_DOWN_TEXT.format(remaining)
            }
            val currentSize = answerMap.size()
            if (currentSize != Constants.QUIZ_SIZE) {
                val remaining = Constants.QUIZ_SIZE - currentSize
                for (i in 1..remaining) {
                    answerMap.put(currentSize + i, false)
                }
            }
            quizViewModel.remainingTime = timerText
            quizViewModel.answerMap.postValue(answerMap)
            dismiss()
        }
    }

    private fun populateQuestionContent() {
        questionCountText = getString(R.string.question_count, count, quizViewModel.questionList.size)
        val (_, question, diff, multiple, answers) = quizViewModel.questionList[count - 1]
        questionText = HtmlCompat.fromHtml(question, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
        difficulty = diff.replaceFirstChar { if (it.isLowerCase()) it.uppercase() else it.toString() }
        isMultiple = multiple
        option1 = HtmlCompat.fromHtml(answers[0].first, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
        option2 = HtmlCompat.fromHtml(answers[1].first, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
        if (multiple) {
            option3 = HtmlCompat.fromHtml(answers[2].first, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
            option4 = HtmlCompat.fromHtml(answers[3].first, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
        } else {
            option3 = null
            option4 = null
        }
    }
}
