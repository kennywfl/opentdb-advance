package com.opentrivia.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.opentrivia.advance.R
import com.opentrivia.advance.databinding.ViewCategoryCountBinding
import com.opentrivia.app.lib.datasource.model.QuestionCount


class CatalogCountAdapter(val context: Context?, val data: MutableList<QuestionCount>) :
    RecyclerView.Adapter<CountHolder>() {

    val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountHolder {
        val view = ViewCategoryCountBinding.inflate(inflater, parent, false)
        return CountHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: CountHolder, position: Int) {
        val item = data[position]
        holder.tvQuestion.text = item.question
        holder.tvTotalCount.text = item.totalCount.toString()
        holder.tvEasyCount.text = context?.getString(R.string.easy_count, item.easyCount)
        holder.tvMediumCount.text = context?.getString(R.string.medium_count, item.mediumCount)
        holder.tvHardCount.text = context?.getString(R.string.hard_count, item.hardCount)
    }

    fun submitList(newData: MutableList<QuestionCount>) {
        var count = data.size
        data.clear()
        notifyItemRangeRemoved(0, count)
        data.addAll(newData)
        count = data.size
        notifyItemRangeInserted(0, count)
    }
}

class CountHolder(view: ViewCategoryCountBinding) : RecyclerView.ViewHolder(view.root) {
    val tvTotalCount = view.tvTotalCount
    val tvQuestion = view.tvQuestion
    val tvEasyCount = view.tvEasyCount
    val tvMediumCount = view.tvMediumCount
    val tvHardCount = view.tvHardCount
}