package com.opentrivia.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.opentrivia.app.R
import com.opentrivia.app.lib.datasource.model.QuestionCount
import kotlinx.android.synthetic.main.view_category_count.view.*


class CatalogCountAdapter(val context: Context?, val data: MutableList<QuestionCount>) :
    RecyclerView.Adapter<CountHolder>() {

    val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountHolder {
        val view = inflater.inflate(R.layout.view_category_count, parent, false)
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

class CountHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvTotalCount = view.tv_total_count
    val tvQuestion = view.tv_question
    val tvEasyCount = view.tv_easy_count
    val tvMediumCount = view.tv_medium_count
    val tvHardCount = view.tv_hard_count
}