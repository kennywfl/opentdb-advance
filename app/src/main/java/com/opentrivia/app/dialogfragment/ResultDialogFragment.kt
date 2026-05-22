package com.opentrivia.app.dialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.core.util.forEach
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.opentrivia.advance.R
import com.opentrivia.app.model.QuizViewModel
import com.opentrivia.app.ui.theme.AppTheme
import com.opentrivia.app.lib.datasource.model.Questions


class ResultDialogFragment : BaseDialogFragment() {

    private val quizViewModel: QuizViewModel by viewModels(ownerProducer = { requireActivity() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.NoTitleDialog)
        populateAnswerCorrectlyFlag()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    QuizResultContent(
                        questions = quizViewModel.questionList,
                        onCloseClick = { dismiss() }
                    )
                }
            }
        }
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

    private fun populateAnswerCorrectlyFlag() {
        quizViewModel.answerMap.value?.forEach { key, value ->
            quizViewModel.questionList[key - 1].answerCorrectly = value
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuizResultContent(
    questions: List<com.opentrivia.app.lib.datasource.model.Questions>,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Results") },
                actions = {
                    IconButton(onClick = onCloseClick) {
                        Icon(Icons.Filled.Close, contentDescription = "Close")
                    }
                }
            )
        },
        modifier = modifier
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            itemsIndexed(questions) { index, question ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Q${index + 1}: ${HtmlCompat.fromHtml(question.question, HtmlCompat.FROM_HTML_MODE_LEGACY)}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = if (question.answerCorrectly) "Correct" else "Incorrect",
                            color = if (question.answerCorrectly) com.opentrivia.app.ui.theme.Green else com.opentrivia.app.ui.theme.Red,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    }
}
