package com.example.sitesyncversionbeta.activityKotlinFiles.adminViewSpecific

import DBHelper
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sitesyncversionbeta.R
import android.widget.AdapterView
import android.widget.ListView
import com.example.sitesyncversionbeta.adapters.EmployeeAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AttendanceAdminViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance_admin_view)

        val dbHelper = DBHelper()

// Call retrieveEmployees from a coroutine scope

        CoroutineScope(Dispatchers.Main).launch {
            // Retrieve employee list on the main thread
            val employeeList = dbHelper.retrieveEmployees()

            // Update UI elements
            val adapter = EmployeeAdapter(
                this@AttendanceAdminViewActivity,
                employeeList
            )
            val listView = findViewById<ListView>(R.id.employeeListView)
            listView.adapter = adapter

            // Set item click listener
            listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val selectedEmployee = adapter.getItem(position)
                val intent = Intent(this@AttendanceAdminViewActivity, EmployeeDetailsActivity::class.java)
                intent.putExtra("employee", selectedEmployee)
                startActivity(intent)
            }
        }

    }
}

