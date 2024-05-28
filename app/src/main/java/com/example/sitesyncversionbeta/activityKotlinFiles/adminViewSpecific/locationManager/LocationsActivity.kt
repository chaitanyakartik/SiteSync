package com.example.sitesyncversionbeta.activityKotlinFiles.adminViewSpecific.locationManager

import DBHelper
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.sitesyncversionbeta.R
import com.example.sitesyncversionbeta.adapters.LocationLocationsActivityAdapter
import com.example.sitesyncversionbeta.databinding.ActivityLocationsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLocationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_locations)

        binding = ActivityLocationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbHelper = DBHelper()

        CoroutineScope(Dispatchers.Main).launch {
            // Retrieve employee list on the main thread
            val locations = dbHelper.retrieveAllLocations()

            val adapter = LocationLocationsActivityAdapter(this@LocationsActivity, locations)
            binding.locationListView.adapter = adapter

            // Get a reference to the GridView and set the adapter
            val listView = findViewById<ListView>(R.id.locationListView)
            listView.adapter = adapter

            // Set item click listener
            listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val selectedLocation = adapter.getItem(position)
                val intent = Intent(this@LocationsActivity, EditLocationsActivity::class.java)
                intent.putExtra("location", selectedLocation)
                startActivity(intent)
            }
        }


        binding.addLocationsButton.setOnClickListener{
            val intent = Intent(this, AddLocationActivity::class.java)
            startActivity(intent)
        }

    }
}
//
//fun createExampleLocations(): List<Location> {
//    val locations = mutableListOf<Location>()
//
//    // Garuda Mall, Bangalore
//    val garudaMall = Location(
//        address = "Magrath Road, Ashok Nagar, Bengaluru, Karnataka 560025, India",
//        latitude = 12.9705,
//        longitude = 77.6089,
//        nickname = "Garuda Mall",
//        locationID = 1,
//        pincode = "560025"
//    )
//    locations.add(garudaMall)
//
//    // Chinnaswamy Stadium, Bangalore
//    val chinnaswamyStadium = Location(
//        locationID = 2,
//        address = "MG Road, Near Anil Kumble Circle, Bengaluru, Karnataka 560001, India",
//        latitude = 12.9784,
//        longitude = 77.5997,
//        nickname = "Chinnaswamy Stadium",
//        pincode = "560001"
//    )
//    locations.add(chinnaswamyStadium)
//
//    return locations
//}