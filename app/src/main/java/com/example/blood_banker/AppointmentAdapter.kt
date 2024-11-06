package com.example.blood_banker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class AppointmentAdapter(private val context: Context, private val appointments: ArrayList<Appointment>) : BaseAdapter() {
    override fun getCount(): Int = appointments.size

    override fun getItem(position: Int): Appointment = appointments[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false)

        val appointment = getItem(position)

        val fullNameTextView = view.findViewById<TextView>(android.R.id.text1)
        val detailsTextView = view.findViewById<TextView>(android.R.id.text2)

        fullNameTextView.text = appointment.fullName
        detailsTextView.text = "Blood Group: ${appointment.bloodGroup}, Location: ${appointment.location}, Contact: ${appointment.contact}"

        return view
    }
}