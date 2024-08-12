package com.opentrivia.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.opentrivia.advance.databinding.ViewSpinnerDummyHintTextBinding
import com.opentrivia.advance.databinding.ViewSpinnerTextviewBinding


class SpinnerAdapter(context: Context, private val dataList: MutableList<Pair<String, String>>) :
    ArrayAdapter<Pair<String, String>>(context, 0, dataList) {

    val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = ViewSpinnerTextviewBinding.inflate(inflater, parent, false)
        val label = view.text1
        val pair = dataList[position]
        label.tag = pair.second
        if (position == 0) {
            label.text = ""
            label.hint = pair.first
        } else {
            label.text = pair.first
        }
        return view.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (position == 0) {
            return ViewSpinnerDummyHintTextBinding.inflate(inflater, parent, false).root
        } else {
            val view = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)
            val label = view.findViewById<TextView>(android.R.id.text1)
            val pair = dataList[position]
            label.text = pair.first
            return view
        }
    }
}