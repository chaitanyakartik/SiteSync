package com.example.sitesyncversionbeta.activityKotlinFiles

import DBHelper
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.sitesyncversionbeta.R
import com.example.sitesyncversionbeta.adapters.LocationLocationsActivityAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PermanentlyDeleteLocationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permanently_delete_locations)

        val dbHelper = DBHelper()

// Call retrieveEmployees from a coroutine scope

        CoroutineScope(Dispatchers.Main).launch {
            // Retrieve employee list on the main thread
            val locationsList = dbHelper.retrieveAllLocations()

            // Update UI elements
            val adapter = LocationLocationsActivityAdapter(
                this@PermanentlyDeleteLocationsActivity,
                locationsList
            )
            val listView = findViewById<ListView>(R.id.locationsListView)
            listView.adapter = adapter

            // Set item click listener
            listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val selectedlocation = adapter.getItem(position)
                val intent = Intent(
                    this@PermanentlyDeleteLocationsActivity,
                    ConfirmLocationDeletionActivity::class.java)
                intent.putExtra("location", selectedlocation)
                startActivity(intent)
            }
        }

    }
}