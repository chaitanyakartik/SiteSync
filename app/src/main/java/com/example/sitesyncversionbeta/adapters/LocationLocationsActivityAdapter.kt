package com.example.sitesyncversionbeta.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.sitesyncversionbeta.R
import com.example.sitesyncversionbeta.dataClassFiles.Location

class LocationLocationsActivityAdapter(
    context: Context,
    private val locations: List<Location>
) : ArrayAdapter<Location>(context, 0, locations) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_location_locations_activity, parent, false)
        }

        val location = locations[position]

        val addressTextView: TextView = itemView!!.findViewById(R.id.addressTextView)
        addressTextView.text = location.address

        val latitudeTextView: TextView = itemView.findViewById(R.id.latitudeTextView)
        latitudeTextView.text = "Latitude: ${location.latitude}"

        val longitudeTextView: TextView = itemView.findViewById(R.id.longitudeTextView)
        longitudeTextView.text = "Longitude: ${location.longitude}"

        val nicknameTextView: TextView = itemView.findViewById(R.id.nicknameTextView)
        nicknameTextView.text = "${location.nickname}"

        val pincodeTextView: TextView = itemView.findViewById(R.id.pincodeTextView)
        pincodeTextView.text = "Pincode: ${location.pincode}"

        val activeTextView: TextView = itemView.findViewById(R.id.activeTextView)
        activeTextView.text = "Active: ${location.active}"

        return itemView
    }
}
