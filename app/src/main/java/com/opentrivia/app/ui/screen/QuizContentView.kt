package com.opentrivia.app.ui.screen

import android.util.SparseBooleanArray
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.core.text.HtmlCompat
import com.opentrivia.app.lib.Constants
import com.opentrivia.app.model.QuizViewModel
import com.opentrivia.app.ui.theme.Red
import kotlinx.coroutines.delay

@Composable
fun QuizContentView(
    quizViewModel: QuizViewModel,
    onDismiss: () -> Unit
) {
    var count by remember { mutableIntStateOf(1) }
    val answerMap = remember { SparseBooleanArray() }

    var questionCountText by remember { mutableStateOf("") }
    var timerText by remember { mutableStateOf("01:00") }
    var timerColor by remember { mutableStateOf(Color.Unspecified) }
    var questionText by remember { mutableStateOf("") }
    var difficulty by remember { mutableStateOf("") }
    var option1 by remember { mutableStateOf("") }
    var option2 by remember { mutableStateOf("") }
    var option3 by remember { mutableStateOf<String?>(null) }
    var option4 by remember { mutableStateOf<String?>(null) }
    var isMultiple by remember { mutableStateOf(true) }

    fun populateQuestionContent() {
        val questions = quizViewModel.questionList
        if (questions.isEmpty() || count - 1 >= questions.size) return
        questionCountText = "Question $count / ${questions.size}"
        val (_, question, diff, multiple, answers) = questions[count - 1]
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

    populateQuestionContent()

    LaunchedEffect(Unit) {
        for (i in 1..Constants.COUNT_DOWN_TIMER) {
            delay(1000)
            val remaining = Constants.COUNT_DOWN_TIMER - i
            timerColor = if (remaining > 5) Color.Unspecified else Red
            timerText = Constants.COUNT_DOWN_TEXT.format(remaining)
        }
        val currentSize = answerMap.size()
        if (currentSize != Constants.QUIZ_SIZE) {
            val remainingQ = Constants.QUIZ_SIZE - currentSize
            for (j in 1..remainingQ) {
                answerMap.put(currentSize + j, false)
            }
        }
        quizViewModel.remainingTime = timerText
        quizViewModel.postAnswerMap(answerMap)
        onDismiss()
    }

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
        onCloseClick = {
            val currentSize = answerMap.size()
            if (currentSize != Constants.QUIZ_SIZE) {
                val remainingQ = Constants.QUIZ_SIZE - currentSize
                for (j in 1..remainingQ) {
                    answerMap.put(currentSize + j, false)
                }
            }
            quizViewModel.remainingTime = timerText
            quizViewModel.postAnswerMap(answerMap)
            onDismiss()
        },
        onOptionClick = { id ->
            answerMap.put(count, id == 1 || id == 2 || id == 3 || id == 4)
            if (count >= Constants.QUIZ_SIZE) {
                quizViewModel.remainingTime = timerText
                quizViewModel.postAnswerMap(answerMap)
                onDismiss()
            } else {
                count++
                populateQuestionContent()
            }
        }
    )
}
