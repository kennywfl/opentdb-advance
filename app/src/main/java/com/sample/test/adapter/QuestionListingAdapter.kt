package com.sample.test.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sample.test.R
import com.sample.test.adapter.model.NetworkState
import com.sample.test.lib.Constants
import com.sample.test.lib.datasource.remote.mapping.response.model.Result
import kotlinx.android.synthetic.main.view_loading.view.*
import kotlinx.android.synthetic.main.view_questions.view.*


const val TYPE_LOADING = 0
const val TYPE_ITEM = 1
const val TYPE_NO_RESULT = 2

class QuestionListingAdapter(val context: Context?) :
    PagedListAdapter<Result, RecyclerView.ViewHolder>(
        DIFF_CALLBACK
    ) {

    private var networkState: NetworkState? = null
    private var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        TYPE_LOADING -> {
            val view = inflater.inflate(R.layout.view_loading, parent, false)
            LoadingHolder(view)
        }
        TYPE_ITEM -> {
            val view = inflater.inflate(R.layout.view_questions, parent, false)
            ItemHolder(view)
        }
        else -> {
            val view = inflater.inflate(R.layout.view_no_record, parent, false)
            NoRecordHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_LOADING -> {

            }
            TYPE_ITEM -> {
                onBindItemHolder(holder as ItemHolder, getItem(position))
            }
            else -> {

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            TYPE_LOADING
        } else {
            if (itemCount == 0) {
                TYPE_NO_RESULT
            } else {
                TYPE_ITEM
            }
        }
    }

    fun onBindItemHolder(holder: ItemHolder, result: Result?) {
        result?.apply {
            holder.tvQuestion.text = HtmlCompat.fromHtml(question, HtmlCompat.FROM_HTML_MODE_LEGACY)
            val diff = when (difficulty) {
                Constants.Api.PARAM_EASY -> context?.getString(R.string.easy)
                Constants.Api.PARAM_MEDIUM -> context?.getString(R.string.medium)
                else -> context?.getString(R.string.hard)
            }
            val ansType = when (type) {
                Constants.Api.PARAM_MULTIPLE -> context?.getString(R.string.multiple_choice)
                else -> context?.getString(R.string.true_false)
            }
            holder.cpDifficulty.text = diff
            holder.cpType.text = ansType
        }
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val previousExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val newExtraRow = hasExtraRow()
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(itemCount)
            } else {
                notifyItemInserted(itemCount)
            }
        } else if (newExtraRow && previousState !== newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Result>() {
            override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem == newItem
            }

        }
    }
}

class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvQuestion = view.tv_question
    val cpDifficulty = view.cp_difficulty
    val cpType = view.cp_answer_type
}

class LoadingHolder(view: View) : RecyclerView.ViewHolder(view) {
    val pbLoading = view.pb_loading
}

class NoRecordHolder(view: View) : RecyclerView.ViewHolder(view) {

}