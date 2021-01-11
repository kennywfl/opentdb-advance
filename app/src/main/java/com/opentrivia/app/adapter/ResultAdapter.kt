package com.opentrivia.app.adapter

import android.content.Context
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.text.buildSpannedString
import androidx.recyclerview.widget.RecyclerView
import com.opentrivia.app.R
import com.opentrivia.app.lib.datasource.model.Questions
import kotlinx.android.synthetic.main.view_result.view.*


class ResultAdapter(val context: Context?, val result: MutableList<Questions>) :
    RecyclerView.Adapter<ResultHolder>() {

    val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultHolder {
        val view = inflater.inflate(R.layout.view_result, parent, false)
        return ResultHolder(view)
    }

    override fun getItemCount() = result.size

    override fun onBindViewHolder(holder: ResultHolder, position: Int) {
        val (_, question, _, _, answers, answerCorrectly) = result[position]
        val displayingQuestion = "${position + 1}. ${HtmlCompat.fromHtml(question, HtmlCompat.FROM_HTML_MODE_LEGACY)}"
        holder.tvQuizQuestion.text = displayingQuestion
        holder.tvQuizAnswer.text = buildSpannedString {
            answers.forEach {
                append("- ")
                if (it.second) {
                    if (answerCorrectly) {
                        context?.let { ctx ->
                            append(
                                HtmlCompat.fromHtml(it.first, HtmlCompat.FROM_HTML_MODE_LEGACY),
                                ForegroundColorSpan(ContextCompat.getColor(ctx, R.color.green)),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }
                    } else {
                        context?.let { ctx ->
                            append(
                                HtmlCompat.fromHtml(it.first, HtmlCompat.FROM_HTML_MODE_LEGACY),
                                ForegroundColorSpan(ContextCompat.getColor(ctx, R.color.red)),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }
                    }
                } else {
                    append(it.first)
                }
                append("\n")
            }
        }
    }
}

class ResultHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvQuizQuestion = view.tv_quiz_question
    val tvQuizAnswer = view.tv_quiz_answer
}