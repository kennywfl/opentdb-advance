package com.sample.test.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.sample.test.R


class SpinnerAdapter(context: Context?, val dataList: MutableList<Pair<String, String>>) :
    ArrayAdapter<Pair<String, String>>(context, 0, dataList) {

    val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = inflater.inflate(R.layout.view_spinner_textview, parent, false)
        val label = view.findViewById<TextView>(android.R.id.text1)
        val pair = dataList[position]
        label.tag = pair.second
        if (position == 0) {
            label.text = ""
            label.hint = pair.first
        } else {
            label.text = pair.first
        }
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (position == 0) {
            return inflater.inflate(R.layout.view_spinner_dummy_hint_text, parent, false)
        } else {
            val view = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)
            val label = view.findViewById<TextView>(android.R.id.text1)
            val pair = dataList[position]
            label.text = pair.first
            return view
        }
    }
}