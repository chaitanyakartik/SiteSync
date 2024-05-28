package com.example.sitesyncversionbeta.activityKotlinFiles.employeeViewSpecific

import DBHelper
import com.example.sitesyncversionbeta.helperClassFiles.LocationHelper
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sitesyncversionbeta.dataClassFiles.Location
import com.example.sitesyncversionbeta.databinding.ActivityConfirmAttendanceBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import android.os.Handler
import com.example.sitesyncversionbeta.R


class ConfirmAttendanceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmAttendanceBinding
    val dbHelper = DBHelper()
    val locationHelper = LocationHelper(this)
    private val handler = Handler()
    private lateinit var attendanceEntryId:String
    private lateinit var locationExtra: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_attendance)


        locationExtra = intent.getSerializableExtra("location") as Location


        binding = ActivityConfirmAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textViewAddress.text = locationExtra.address
        binding.textViewNickname.text = locationExtra.nickname
        binding.textViewPincode.text = locationExtra.pincode


        binding.confirmAttendanceButton.setOnClickListener {

            var distance = 0.000000000000
            locationHelper.getCurrentLocation { location ->
                location?.let { (currentLatitude, currentLongitude, accuracy) ->
                    // Now you have latitude and longitude, you can use them directly
                    distance = dbHelper.calculateDistance(
                        locationExtra.latitude,
                        locationExtra.longitude,
                        currentLatitude,
                        currentLongitude
                    )

                    if (distance == 0.000000000000) {
                        Toast.makeText(
                            this@ConfirmAttendanceActivity,
                            "There has been an error, distance not calculated$distance and $PERMISSIBLE_DISTANCE",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
//most imp part
                    else if (distance < PERMISSIBLE_DISTANCE) {
                        CoroutineScope(Dispatchers.Main).launch {
                            attendanceEntryId = dbHelper.logAttendance(EMPLOYEE_ID, locationExtra.nickname,locationExtra.latitude, locationExtra.longitude)
                        }
                        Toast.makeText(this@ConfirmAttendanceActivity,"Your attendance is logged$distance and $PERMISSIBLE_DISTANCE",Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(
                            this@ConfirmAttendanceActivity,
                            "Error logging attendance in, too far from target location$distance and $PERMISSIBLE_DISTANCE",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }



    }


//    private fun startLocationUpdates() {
//        handler.postDelayed(object : Runnable {
//            override fun run() {
//                locationHelper.getCurrentLocation { location ->
//                    location?.let { (currentLatitude, currentLongitude, accuracy) ->
//                        // Now you have latitude and longitude, you can use them directly
//                        // Calculate distance between current location and target location
//                        val distance = dbHelper.calculateDistance(
//                            currentLatitude,
//                            currentLatitude,
//                            locationExtra.latitude,
//                            locationExtra.longitude
//                        )
//
//                        // Check if distance exceeds the threshold
//                        if (distance > DISTANCE_THRESHOLD) {
//                            // User has left the target location, log checkout time
//                            logCheckoutTime()
//                        } else {
//                            // User is still within the target location, continue monitoring
//                            // Run this task again after a certain interval (e.g., every 5 minutes)
//                            handler.postDelayed(this, INTERVAL_BETWEEN_UPDATES)
//                        }
//                    }
//                }
//            }
//        }, INTERVAL_BETWEEN_UPDATES)
//    }
//
//    private fun logCheckoutTime() {
//        CoroutineScope(Dispatchers.Main).launch {
//            dbHelper.updateCheckoutTime(EMPLOYEE_ID, attendanceEntryId)
//        }
//    }
//
//
    override fun onDestroy() {
        super.onDestroy()
        // Remove any pending callbacks to avoid memory leaks
        handler.removeCallbacksAndMessages(null)
    }

    companion object {
        // Constants for distance threshold and interval between updates
        private const val DISTANCE_THRESHOLD = 100 // Example distance threshold in meters
        private const val INTERVAL_BETWEEN_UPDATES = 300000L // Example interval in milliseconds (5 minutes)
        private const val EMPLOYEE_ID = "employee2"
        const val PERMISSIBLE_DISTANCE = 0.2
    }

}