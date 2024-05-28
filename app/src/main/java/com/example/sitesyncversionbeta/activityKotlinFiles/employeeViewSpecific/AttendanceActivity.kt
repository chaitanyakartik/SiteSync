package com.example.sitesyncversionbeta.activityKotlinFiles.employeeViewSpecific

import DBHelper
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.sitesyncversionbeta.adapters.AttendanceAdapter
import com.example.sitesyncversionbeta.R
import com.example.sitesyncversionbeta.databinding.ActivityAttendanceBinding
import com.example.sitesyncversionbeta.helperClassFiles.LocationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import java.time.LocalDate

class AttendanceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAttendanceBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance)

        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbHelper = DBHelper()

// Call retrieveEmployees from a coroutine scope

        CoroutineScope(Dispatchers.Main).launch {
            // Retrieve employee list on the main thread
            val attendanceList = dbHelper.retrieveEmployeeAttendance("employee2")

            val sortedAttendanceList =
                attendanceList.sortedWith(compareBy({ it.date }, { it.checkinTime })).reversed()

            //setup whole attendance gridview
            val adapter = AttendanceAdapter(
                this@AttendanceActivity,
                R.layout.item_attendance,
                sortedAttendanceList
            )
            binding.attendanceGridView.adapter = adapter

            val latestEntry = sortedAttendanceList.firstOrNull()

            if (latestEntry != null) {
                if (latestEntry.checkoutTime.isEmpty() && isLatestEntryToday(latestEntry.date)) {
                    binding.textViewDate.text = "Date: " + latestEntry.date
                    binding.textViewLocation.text = latestEntry.locationNickname
                    binding.textViewCheckInTime.text = "Check-in Time: " + latestEntry.checkinTime
                    binding.imageViewLogout.setImageResource(R.drawable.my_shaped_button)

                    binding.imageViewLogout.setOnClickListener {

                        //

                        var distance = 0.000000000000
                        val locationHelper = LocationHelper(this@AttendanceActivity)

                        locationHelper.getCurrentLocation { location ->
                            location?.let { (currentLatitude, currentLongitude, accuracy) ->
                                // Now you have latitude and longitude, you can use them directly
                                distance = dbHelper.calculateDistance(
                                    latestEntry.locationLatitude,
                                    latestEntry.locationLongitude,
                                    currentLatitude,
                                    currentLongitude
                                )

                                if (distance == 0.000000000000) {
                                    Toast.makeText(
                                        this@AttendanceActivity,
                                        "There has been an error, distance not calculated$distance and ${ConfirmAttendanceActivity.PERMISSIBLE_DISTANCE}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
//most imp part
                                else if (distance < ConfirmAttendanceActivity.PERMISSIBLE_DISTANCE) {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        val currentTime = getCurrentTime()
                                        val newCheckoutTime = currentTime

                                        val timeDifference = calculateTimeDifference(
                                            latestEntry.checkinTime,
                                            newCheckoutTime
                                        )

                                        CoroutineScope(Dispatchers.Main).launch {
                                            dbHelper.updateCheckoutTime(
                                                employeeId = latestEntry.employeeID,
                                                attendanceEntryId = latestEntry.id,
                                                newCheckoutTime = newCheckoutTime,
                                                totalTime = timeDifference
                                            )
                                        }
                                        Toast.makeText(
                                            this@AttendanceActivity,
                                            "Check-Out Time has been logged in $distance and $PERMISSIBLE_DISTANCE",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        this@AttendanceActivity,
                                        "Error logging attendance in, too far from target location$distance and $PERMISSIBLE_DISTANCE",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }


                }
            }
        }
    }

    companion object {
        // Constants for distance threshold and interval between updates
        private const val DISTANCE_THRESHOLD = 100 // Example distance threshold in meters
        private const val INTERVAL_BETWEEN_UPDATES =
            300000L // Example interval in milliseconds (5 minutes)
        private const val PERMISSIBLE_DISTANCE = 0.2
    }
}

private fun getCurrentTime(): String {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(Date())
}


fun isValidTimeFormat(time: String): Boolean {
    return try {
        val dateFormat = SimpleDateFormat("HH:mm")
        dateFormat.isLenient = false
        dateFormat.parse(time)
        true
    } catch (e: Exception) {
        false
    }
}

fun calculateTimeDifference(checkinTime: String, checkoutTime: String): String {
    val dateFormat = SimpleDateFormat("HH:mm")
    val checkinDateTime = dateFormat.parse(checkinTime)
    val checkoutDateTime = if (checkoutTime.isNotBlank() && isValidTimeFormat(checkoutTime)) {
        dateFormat.parse(checkoutTime)
    } else {
        Date()
    }

    // Calculate the difference in milliseconds
    val diffInMillis = checkoutDateTime.time - checkinDateTime.time

    // Convert milliseconds to hours and minutes
    val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis) % 60

    return String.format("%02d:%02d", hours, minutes)
}

@RequiresApi(Build.VERSION_CODES.O)
fun isLatestEntryToday(latestEntryDate: String): Boolean {
    // Get today's date
    val todayDate = LocalDate.now()
    // Check if latest entry's date is today's date
    return latestEntryDate == todayDate.toString()
}



