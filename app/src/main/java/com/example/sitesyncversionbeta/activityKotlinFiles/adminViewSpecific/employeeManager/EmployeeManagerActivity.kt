package com.example.sitesyncversionbeta.activityKotlinFiles.adminViewSpecific.employeeManager

import DBHelper
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.example.sitesyncversionbeta.R
import com.example.sitesyncversionbeta.adapters.EmployeeAdapter
import com.example.sitesyncversionbeta.databinding.ActivityEmployeeManagerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EmployeeManagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmployeeManagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_manager)

        binding = ActivityEmployeeManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbHelper = DBHelper()

// Call retrieveEmployees from a coroutine scope

        CoroutineScope(Dispatchers.Main).launch {
            // Retrieve employee list on the main thread
            val employeeList = dbHelper.retrieveEmployees()

            // Update UI elements
            val adapter = EmployeeAdapter(
                this@EmployeeManagerActivity,
                employeeList
            )
            binding.employeeListView.adapter = adapter

            // Set item click listener
            binding.employeeListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val selectedEmployee = adapter.getItem(position)
                val intent = Intent(this@EmployeeManagerActivity, EditEmployeeActivity::class.java)
                intent.putExtra("employee", selectedEmployee)
                startActivity(intent)
            }

        }
        binding.addEmployeeButton.setOnClickListener{
            val intent = Intent(this, AddEmployeeActivity::class.java)
            startActivity(intent)
        }
    }
}