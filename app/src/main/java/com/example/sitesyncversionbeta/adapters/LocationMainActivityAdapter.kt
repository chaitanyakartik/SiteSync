package com.example.sitesyncversionbeta.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.sitesyncversionbeta.R
import com.example.sitesyncversionbeta.dataClassFiles.Location

class LocationMainActivityAdapter(
    context: Context,
    private val locations: List<Location>
) : ArrayAdapter<Location>(context, 0, locations) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_location_main_activity, parent, false)
        }

        val location = locations[position]

        val nicknameTextView: TextView = itemView!!.findViewById(R.id.nicknameTextView)
        nicknameTextView.text = location.nickname

        val pincodeTextView: TextView = itemView.findViewById(R.id.pincodeTextView)
        pincodeTextView.text = "Pincode: ${location.latitude}"

        return itemView
    }
}
