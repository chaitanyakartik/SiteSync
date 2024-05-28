package com.example.sitesyncversionbeta.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.sitesyncversionbeta.R
import com.example.sitesyncversionbeta.dataClassFiles.AttendanceEntry

class AttendanceAdapter(context: Context, resource: Int, objects: List<AttendanceEntry>) :
    ArrayAdapter<AttendanceEntry>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = convertView ?: inflater.inflate(R.layout.item_attendance, parent, false)

        // Get references to TextViews in the layout
        val dateTextView = view.findViewById<TextView>(R.id.dateTextView)
        val checkInTextView = view.findViewById<TextView>(R.id.checkInTextView)
        val checkOutTextView = view.findViewById<TextView>(R.id.checkOutTextView)
        val locationTextView = view.findViewById<TextView>(R.id.locationTextView)
        val totalTimeTextView = view.findViewById<TextView>(R.id.totalTimeTextView)

        // Get the AttendanceEntry object for this position
        val attendanceEntry = getItem(position)

        // Display the data in the TextViews
        if (attendanceEntry != null) {
            dateTextView.text = "Date: "+attendanceEntry.date
            checkInTextView.text = "In: "+attendanceEntry.checkinTime
            checkOutTextView.text = "Out: "+attendanceEntry.checkoutTime
            locationTextView.text = "Location: "+attendanceEntry.locationNickname
            totalTimeTextView.text = "Total Time: "+ attendanceEntry.totalTime
        }
        return view
    }
}


