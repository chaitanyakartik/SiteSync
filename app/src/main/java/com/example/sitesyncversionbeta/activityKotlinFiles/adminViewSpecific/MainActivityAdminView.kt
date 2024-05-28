package com.example.sitesyncversionbeta.activityKotlinFiles.adminViewSpecific

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.example.sitesyncversionbeta.R
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.sitesyncversionbeta.activityKotlinFiles.adminViewSpecific.employeeManager.EmployeeManagerActivity
import com.example.sitesyncversionbeta.activityKotlinFiles.adminViewSpecific.locationManager.LocationsActivity
import com.example.sitesyncversionbeta.LoginActivity
import com.example.sitesyncversionbeta.activityKotlinFiles.EditUserProfileActivity
import com.example.sitesyncversionbeta.dataClassFiles.Employee

import com.example.sitesyncversionbeta.databinding.ActivityMainAdminViewBinding


class MainActivityAdminView : AppCompatActivity() {

    private lateinit var binding: ActivityMainAdminViewBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAdminViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userDetails = intent.getSerializableExtra("UserDetails") as? Employee

        // Set click listener for bottom navigation items
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_settings -> {
                    // Open MessagingActivity
                    val intent = Intent(this@MainActivityAdminView, EditUserProfileActivity::class.java)
                    intent.putExtra("UserDetails", userDetails)
                    startActivity(intent)
                    true
                }
                R.id.navigation_attendance -> {
                    // Open AttendanceActivity
                    startActivity(Intent(this, AttendanceAdminViewActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Set the toolbar as the action bar
        setSupportActionBar(binding.toolbar)
        // Enable the Up button (back button) in the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                // Handle action_item2 click
                showToast("Logout button clicked")
                startActivity(Intent(this, LoginActivity::class.java))
                true
            }
            R.id.employee_manager -> {
                // Handle action_item2 click
                val intent = Intent(this, EmployeeManagerActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.locations_manager -> {
                // Handle action_item2 click
                val intent = Intent(this, LocationsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu_admin_view, menu)
        return true
    }
}






