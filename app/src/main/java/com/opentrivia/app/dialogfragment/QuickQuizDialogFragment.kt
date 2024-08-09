package com.opentrivia.app.dialogfragment

import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.opentrivia.advance.R
import com.opentrivia.advance.databinding.FragmentQuestionsBinding
import com.opentrivia.app.extension.hide
import com.opentrivia.app.extension.show
import com.opentrivia.app.framework.model.QuizViewModel
import com.opentrivia.app.framework.presenter.QuickQuizPresenter
import com.opentrivia.app.framework.view.QuickQuizView
import com.opentrivia.app.lib.Constants
import java.util.Locale
import javax.inject.Inject


class QuickQuizDialogFragment : BaseDialogFragment(), QuickQuizView, View.OnClickListener {

    @Inject
    lateinit var presenter: QuickQuizPresenter
    private lateinit var quizViewModel: QuizViewModel
    private var count = 1
    private val buttonMap = SparseBooleanArray()
    private val answerMap = SparseBooleanArray()
    private lateinit var binding: FragmentQuestionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizViewModel = activity?.run {
            ViewModelProviders.of(this).get(QuizViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.NoTitleDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentQuestionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivClose.setOnClickListener {
            dismiss()
        }
        binding.cvOption1.setOnClickListener(this)
        binding.cvOption2.setOnClickListener(this)
        binding.cvOption3.setOnClickListener(this)
        binding.cvOption4.setOnClickListener(this)
        populateQuestionContent()
        presenter.startTimer()
    }

    override fun onStart() {
        super.onStart()
        presenter.bindView(this)
        val window = dialog?.window
        if (window != null) {
            val params = window.attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.MATCH_PARENT
            window.attributes = params as WindowManager.LayoutParams
        }
    }

    override fun onStop() {
        super.onStop()
        presenter.unbindView()
    }

    override fun onTimeCountDown(remainingTime: Long) {
        if (remainingTime > 5) {
            context?.let {
                binding.tvTimer.setTextColor(ContextCompat.getColor(it, R.color.textPrimary))
            }
        } else {
            context?.let {
                binding.tvTimer.setTextColor(ContextCompat.getColor(it, R.color.red))
            }
            if (remainingTime != 0L) {
                binding.tvTimer.animate()
                    .setDuration(400)
                    .alpha(0f)
                    .withEndAction {
                        binding.tvTimer.animate()
                            .alpha(1f)
                            .setDuration(400)
                    }
                    .start()
            }
        }
        binding.tvTimer.text = Constants.COUNT_DOWN_TEXT.format(remainingTime)
    }

    override fun onCountDownFinished() {
        val currentSize = answerMap.size()
        if (currentSize != Constants.QUIZ_SIZE) {
            val remaining = Constants.QUIZ_SIZE - currentSize
            for (i in 1..remaining) {
                answerMap.put(currentSize + i, false)
            }
        }
        quizViewModel.remainingTime = binding.tvTimer.text.toString()
        quizViewModel.answerMap.postValue(answerMap)
        dismiss()
    }

    override fun onClick(v: View?) {
        v?.let {
            val result = buttonMap[it.id]
            answerMap.put(count, result)
            if (count > Constants.QUIZ_SIZE - 1) {
                quizViewModel.remainingTime = binding.tvTimer.text.toString()
                quizViewModel.answerMap.postValue(answerMap)
                dismiss()
            } else {
                count++
                populateQuestionContent()
            }
        }
    }

    fun updateQuestionCount() {
        binding.tvQuestionCount.text =
            getString(R.string.question_count, count, quizViewModel.questionList.size)
    }

    fun populateQuestionContent() {
        buttonMap.clear()
        updateQuestionCount()
        val (_, question, difficulty, isMultiple, answers) = quizViewModel.questionList[count - 1]
        binding.tvQuestion.text = HtmlCompat.fromHtml(question, HtmlCompat.FROM_HTML_MODE_LEGACY)
        constructDifficulty(difficulty)
        binding.tvOption1.text =
            HtmlCompat.fromHtml(answers[0].first, HtmlCompat.FROM_HTML_MODE_LEGACY)
        buttonMap.put(R.id.cv_option_1, answers[0].second)
        binding.tvOption2.text =
            HtmlCompat.fromHtml(answers[1].first, HtmlCompat.FROM_HTML_MODE_LEGACY)
        buttonMap.put(R.id.cv_option_2, answers[1].second)
        if (isMultiple) {
            binding.tvOption3.text =
                HtmlCompat.fromHtml(answers[2].first, HtmlCompat.FROM_HTML_MODE_LEGACY)
            buttonMap.put(R.id.cv_option_3, answers[2].second)
            binding.cvOption3.show()
            binding.tvOption4.text =
                HtmlCompat.fromHtml(answers[3].first, HtmlCompat.FROM_HTML_MODE_LEGACY)
            buttonMap.put(R.id.cv_option_4, answers[3].second)
            binding.cvOption4.show()
        } else {
            binding.cvOption3.hide()
            binding.cvOption4.hide()
        }
    }

    private fun constructDifficulty(difficulty: String) {
        binding.tvDifficulty.text = difficulty.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
        binding.tvDifficulty.show()
        if (Constants.Api.PARAM_EASY.equals(difficulty)) {
            binding.tvDifficulty.setChipBackgroundColorResource(R.color.green)
        } else if (Constants.Api.PARAM_MEDIUM.equals(difficulty)) {
            binding.tvDifficulty.setChipBackgroundColorResource(R.color.orange)
        } else if (Constants.Api.PARAM_HARD.equals(difficulty)) {
            binding.tvDifficulty.setChipBackgroundColorResource(R.color.red)
        } else {
            binding.tvDifficulty.hide()
        }
    }

}