package com.example.sitesyncversionbeta.activityKotlinFiles.adminViewSpecific

import DBHelper
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sitesyncversionbeta.R
import com.example.sitesyncversionbeta.adapters.AttendanceAdapter
import com.example.sitesyncversionbeta.adapters.LocationMainActivityAdapter
import com.example.sitesyncversionbeta.dataClassFiles.Employee
import com.example.sitesyncversionbeta.databinding.ActivityEmployeeDetailsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EmployeeDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmployeeDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_details)

        val employee = intent.getSerializableExtra("employee") as Employee

        binding = ActivityEmployeeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Display employee details using employee object
        binding.textViewName.text = employee.name
        //binding.imageViewProfilePic.setImageResource(employee.profilePictureID)
        binding.textViewEmail.text = "Email: "+employee.email
        binding.textViewNumber.text = "Number: "+employee.number

        val adapterAttendance = AttendanceAdapter(
            this,
            R.layout.item_attendance,
            employee.attendanceEntries.toList().map { it.second }
        )
        binding.attendanceAdminGridView.adapter = adapterAttendance


        val dbHelper = DBHelper()
        //Populate the locations list
        CoroutineScope(Dispatchers.Main).launch {
            // Retrieve employee list on the main thread
            val locationIds = employee.assignedLocations

            val locations = dbHelper.retrieveLocations(locationIds)

            val adapterLocations = LocationMainActivityAdapter(
                this@EmployeeDetailsActivity,
                locations
            )
            binding.locationsListView.adapter = adapterLocations

        }




    }
}
