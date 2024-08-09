package com.opentrivia.app.dialogfragment

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
import com.opentrivia.advance.R
import com.opentrivia.advance.databinding.FragmentResultBinding
import com.opentrivia.app.adapter.ResultAdapter
import com.opentrivia.app.framework.model.QuizViewModel


class ResultDialogFragment : BaseDialogFragment() {

    private lateinit var quizViewModel: QuizViewModel
    private lateinit var binding: FragmentResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.NoTitleDialog)
        quizViewModel = activity?.run {
            ViewModelProviders.of(this).get(QuizViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        populateAnswerCorrectlyFlag()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivClose.setOnClickListener {
            dismiss()
        }
        val adapter = ResultAdapter(context, quizViewModel.questionList)
        binding.rvResult.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvResult.adapter = adapter
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