package com.example.sitesyncversionbeta.activityKotlinFiles.employeeViewSpecific

import DBHelper
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.Menu
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.sitesyncversionbeta.R
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.sitesyncversionbeta.LoginActivity
import com.example.sitesyncversionbeta.activityKotlinFiles.EditUserProfileActivity
import com.example.sitesyncversionbeta.adapters.LocationMainActivityAdapter
import com.example.sitesyncversionbeta.dataClassFiles.Employee

import com.example.sitesyncversionbeta.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var EmployeeId: String = "employee2"

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userDetails = intent.getSerializableExtra("UserDetails") as? Employee
        if (userDetails != null) {
            binding.textViewName.text = userDetails.name
            binding.textViewEmail.text = userDetails.email
            binding.textViewNumber.text = userDetails.number
            EmployeeId = userDetails.employeeId
        }

        //Setting up all details



        // Set click listener for bottom navigation items
        binding.bottomNavigationView.setOnItemSelectedListener  { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_settings -> {
                    // Open Settings activity
                    val intent = Intent(this@MainActivity, EditUserProfileActivity::class.java)
                    intent.putExtra("UserDetails", userDetails)
                    startActivity(intent)
                    return@setOnItemSelectedListener  true
                }
                R.id.navigation_attendance -> {
                    // Open AttendanceActivity
                    startActivity(Intent(this, AttendanceActivity::class.java))
                    return@setOnItemSelectedListener  true
                }
                else -> false
            }
        }

        // Set the toolbar as the action bar
        setSupportActionBar(binding.toolbar)
        // Enable the Up button (back button) in the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Firebase database used to get locations
        val dbHelper = DBHelper()
        CoroutineScope(Dispatchers.Main).launch {
            // Retrieve employee list on the main thread
            val locationIds = dbHelper.retrieveAssignedLocationsIds(EmployeeId)

            val locations = dbHelper.retrieveLocations(locationIds)

            val adapter = LocationMainActivityAdapter(this@MainActivity, locations)
            binding.assignedLocationsListView.adapter = adapter

            // Get a reference to the GridView and set the adapter
            val listView = findViewById<ListView>(R.id.assignedLocationsListView)
            listView.adapter = adapter

            // Set item click listener
            listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val selectedLocation = adapter.getItem(position)
                val intent = Intent(this@MainActivity, ConfirmAttendanceActivity::class.java)
                intent.putExtra("location", selectedLocation)
                startActivity(intent)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                // Handle action_item2 click
                showToast("Logout button clicked")
                startActivity(Intent(this, LoginActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

}

