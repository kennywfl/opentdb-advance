package com.opentrivia.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.opentrivia.advance.R
import com.opentrivia.advance.databinding.ViewLoadingBinding
import com.opentrivia.advance.databinding.ViewNoRecordBinding
import com.opentrivia.advance.databinding.ViewQuestionsBinding
import com.opentrivia.app.adapter.model.NetworkState
import com.opentrivia.app.lib.Constants
import com.opentrivia.app.lib.datasource.remote.mapping.response.model.Result


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
            val view = ViewLoadingBinding.inflate(inflater, parent, false)
            LoadingHolder(view)
        }
        TYPE_ITEM -> {
            val view = ViewQuestionsBinding.inflate(inflater, parent, false)
            ItemHolder(view)
        }
        else -> {
            val view = ViewNoRecordBinding.inflate(inflater, parent, false)
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

    private fun onBindItemHolder(holder: ItemHolder, result: Result?) {
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

class ItemHolder(view: ViewQuestionsBinding) : RecyclerView.ViewHolder(view.root) {
    val tvQuestion = view.tvQuestion
    val cpDifficulty = view.cpDifficulty
    val cpType = view.cpAnswerType
}

class LoadingHolder(view: ViewLoadingBinding) : RecyclerView.ViewHolder(view.root) {
    val pbLoading = view.pbLoading
}

class NoRecordHolder(view: ViewNoRecordBinding) : RecyclerView.ViewHolder(view.root)