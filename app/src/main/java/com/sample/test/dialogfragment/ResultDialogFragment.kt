package com.sample.test.dialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.util.forEach
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.test.R
import com.sample.test.adapter.ResultAdapter
import com.sample.test.framework.model.QuizViewModel
import kotlinx.android.synthetic.main.fragment_result.*


class ResultDialogFragment : BaseDialogFragment() {

    private lateinit var quizViewModel: QuizViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.NoTitleDialog)
        quizViewModel = activity?.run {
            ViewModelProviders.of(this).get(QuizViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        populateAnswerCorrectlyFlag()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_close.setOnClickListener {
            dismiss()
        }
        val adapter = ResultAdapter(context, quizViewModel.questionList)
        rv_result.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rv_result.adapter = adapter
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

    fun populateAnswerCorrectlyFlag() {
        quizViewModel.answerMap.value?.forEach { key, value ->
            quizViewModel.questionList[key - 1].answerCorrectly = value
        }
    }

}