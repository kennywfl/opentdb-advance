package com.opentrivia.app.dialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.core.util.forEach
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opentrivia.advance.R
import com.opentrivia.app.adapter.ResultAdapter
import com.opentrivia.app.framework.model.QuizViewModel
import com.opentrivia.app.ui.screen.QuizResultScreen
import com.opentrivia.app.ui.theme.AppTheme


class ResultDialogFragment : BaseDialogFragment() {

    private lateinit var quizViewModel: QuizViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.NoTitleDialog)
        quizViewModel = activity?.run {
            ViewModelProviders.of(this).get(QuizViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        populateAnswerCorrectlyFlag()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val adapter = ResultAdapter(context, quizViewModel.questionList)
        recyclerView = RecyclerView(requireContext()).apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            this.adapter = adapter
        }

        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    QuizResultScreen(
                        recyclerView = recyclerView,
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
