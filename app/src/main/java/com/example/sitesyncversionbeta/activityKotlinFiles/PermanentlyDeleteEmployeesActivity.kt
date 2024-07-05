package com.example.sitesyncversionbeta.activityKotlinFiles

import DBHelper
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sitesyncversionbeta.R
import com.example.sitesyncversionbeta.adapters.EmployeeAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PermanentlyDeleteEmployeesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permanently_delete_employees)

        val dbHelper = DBHelper()

// Call retrieveEmployees from a coroutine scope

        CoroutineScope(Dispatchers.Main).launch {
            // Retrieve employee list on the main thread
            val employeeList = dbHelper.retrieveEmployees()

            // Update UI elements
            val adapter = EmployeeAdapter(
                this@PermanentlyDeleteEmployeesActivity,
                employeeList
            )
            val listView = findViewById<ListView>(R.id.employeeListView)
            listView.adapter = adapter

            // Set item click listener
            listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val selectedEmployee = adapter.getItem(position)
                if (selectedEmployee != null) {
                    if (selectedEmployee.email == "chaitanyakartikm@gmail.com"){
                        Toast.makeText(this@PermanentlyDeleteEmployeesActivity, "Cannot delete this user", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        val intent = Intent(
                            this@PermanentlyDeleteEmployeesActivity,
                            ConfirmEmployeeDeletionActivity::class.java
                        )
                        intent.putExtra("employee", selectedEmployee)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}