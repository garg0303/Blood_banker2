package com.example.blood_banker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class EmergencyAdapter(private val context: Context, private val emergencies: List<Emergency>) : BaseAdapter() {
    override fun getCount(): Int = emergencies.size

    override fun getItem(position: Int): Emergency = emergencies[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
        val emergency = getItem(position)

        val detailsTextView = view.findViewById<TextView>(android.R.id.text1)
        detailsTextView.text = emergency.details

        return view
    }

}