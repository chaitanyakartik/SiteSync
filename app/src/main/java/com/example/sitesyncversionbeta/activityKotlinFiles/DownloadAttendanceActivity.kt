package com.example.sitesyncversionbeta.activityKotlinFiles

import DBHelper
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.sitesyncversionbeta.databinding.ActivityDownloadAttendanceBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.sitesyncversionbeta.dataClassFiles.AttendanceEntry
import com.example.sitesyncversionbeta.dataClassFiles.Employee
import java.time.LocalDate


import jxl.Workbook
import jxl.write.Label
import jxl.write.WritableWorkbook
import jxl.write.WriteException
import java.io.File
import java.io.IOException

class DownloadAttendanceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDownloadAttendanceBinding
    private lateinit var database: DatabaseReference
    private lateinit var dbHelper: DBHelper

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDownloadAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper()

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference.child("employees")

        // Set up spinners
        setupSpinners()

        //when button is clicked
        binding.btnDownloadXML.setOnClickListener {

            CoroutineScope(Dispatchers.Main).launch {
                var allEmployeeDetailsList = dbHelper.retrieveEmployees()
                if (binding.spinnerEmployee.selectedItem.toString() == "All Employees") {

                } else {
                    // Assuming allEmployeeDetailsList is a List<Employee?>
                    val selectedEmployeeName = binding.spinnerEmployee.selectedItem.toString()
                    val selectedEmployee =
                        allEmployeeDetailsList.find { it?.name == selectedEmployeeName }
                    allEmployeeDetailsList =
                        if (selectedEmployee != null) listOf(selectedEmployee) else emptyList()

                }

                allEmployeeDetailsList = filterEmployeeAttendanceListByTimeFrameSpinnerSelection(allEmployeeDetailsList)

                allEmployeeDetailsList = sortEmployeeAttendanceListByDate(allEmployeeDetailsList).filterNotNull()

                val directory =  this@DownloadAttendanceActivity.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                //val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val fileName = File(directory, "attendance_data.xls").absolutePath

                Log.d("SEEE THIS 3 ", allEmployeeDetailsList.toString())

                writeAttendanceDataToExcel(allEmployeeDetailsList, fileName)

            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun filterEmployeeAttendanceListByTimeFrameSpinnerSelection(allEmployeeDetailsList: List<Employee>): List<Employee> {
        // Iterate over each employee in the list
        var finalEmployeeList = allEmployeeDetailsList
        for (i in finalEmployeeList.indices) {

            val filteredList = filterAttendanceEntriesByTimeFrame(
                finalEmployeeList[i].attendanceEntries.toList().map { it.second },
                binding.spinnerTimeDuration.selectedItem.toString()
            )
            // Check if the attendance entry is within the time frame
            Log.d("SEEE THIS Some details", binding.spinnerTimeDuration.selectedItem.toString())
            val filteredMap: Map<String, AttendanceEntry> = filteredList.associateBy { it.id }
            finalEmployeeList[i].attendanceEntries = filteredMap
        }
        return finalEmployeeList
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun filterAttendanceEntriesByTimeFrame(
        attendanceEntries: List<AttendanceEntry>?,
        timeFrame: String
    ): List<AttendanceEntry> {
        // Return empty list if input is null or empty
        if (attendanceEntries.isNullOrEmpty()) {
            Log.d("SEEE THIS (attendance entries null)", "nm")
            return emptyList()
        }

        // Get the current month
        val currentMonth = LocalDate.now().monthValue

        // Initialize the filtered list
        val filteredList = mutableListOf<AttendanceEntry>()

        // Iterate through the attendance entries
        for (attendanceEntry in attendanceEntries) {
            // Get the month from the attendance entry date
            val attendanceMonth: Int? = try {
                attendanceEntry.date.split("-")[1].toIntOrNull()
            } catch (e: Exception) {
                null
            }

            // Filter the entries based on the selected time frame
            when (timeFrame) {
                "This Month" -> {
                    if (attendanceMonth == currentMonth) {
                        filteredList.add(attendanceEntry) // Add entry to filtered list if within the current month
                        Log.d("SEEE THIS (attendance rntyry)", attendanceEntry.toString())

                    }
                }

                "Last 3 Months" -> {
                    if (attendanceMonth != null) {
                        val monthsAgo = LocalDate.now().minusMonths(3).monthValue
                        if (attendanceMonth in monthsAgo..currentMonth) {
                            filteredList.add(attendanceEntry) // Add entry to filtered list if within the last 3 months
                        }
                    }
                }

                "All Time" -> {
                    filteredList.add(attendanceEntry) // Add all entries for "All Time" frame
                }
            }
        }

        return filteredList // Return filtered list
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun sortEmployeeAttendanceListByDate(allEmployeeDetailsList: List<Employee?>): List<Employee?> {
        return allEmployeeDetailsList.map { employee ->
            // Sort the attendance entries for each employee
            val sortedAttendanceEntries = employee?.attendanceEntries
                ?.toList() // Convert map entries to a list
                ?.sortedBy { (_, attendanceEntry) ->
                    val (year, month, day) = attendanceEntry?.date?.split("-") ?: listOf("", "", "")
                    // Create a comparable date object for sorting
                    LocalDate.of(year.toInt(), month.toInt(), day.toInt())
                }
                ?.toMap() // Convert sorted list back to a map

            // Create a new employee object with sorted attendance entries
            employee?.copy(attendanceEntries = sortedAttendanceEntries.orEmpty())
        }
    }


    private fun setupSpinners() {
        // Hardcoded time durations
        val timeDurations = listOf("This Month", "Last 3 Months", "All Time")
        val timeDurationAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, timeDurations)
        timeDurationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTimeDuration.adapter = timeDurationAdapter

        // Fetch employees from Firebase
        val allEmployees = mutableListOf("All Employees")

        CoroutineScope(Dispatchers.Main).launch {
            // Retrieve employee list on the main thread
            val employeeList = dbHelper.retrieveEmployees()
            val employeeNames = employeeList.map { it.name }
            allEmployees.addAll(employeeNames)
        }

        val employeeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, allEmployees)
        employeeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerEmployee.adapter = employeeAdapter
    }

    fun writeAttendanceDataToExcel(employees: List<Employee>, fileName: String) {
        val workbook: WritableWorkbook = Workbook.createWorkbook(File(fileName))
        val sheet = workbook.createSheet("Attendance Data", 0)

        var row = 0

        for (employee in employees) {
            // Write employee details (name and number)
            val nameAndNumberLabel = Label(
                0,
                row,
                "Employee Name: ${employee.name}, Employee Number: ${employee.number}"
            )
            sheet.addCell(nameAndNumberLabel)
            row++

            // Write attendance entries
            for ((_, entry) in employee.attendanceEntries) {
                val dateLabel = Label(0, row, "Attendance Entry Date: ${entry.date}")
                val checkInLabel = Label(1, row, "Check-in Time: ${entry.checkinTime}")
                val checkOutLabel = Label(2, row, "Checkout Time: ${entry.checkoutTime}")
                val locationLabel = Label(3, row, "Location Name: ${entry.locationNickname}")
                val totalTimeLabel = Label(4, row, "Total Time: ${entry.totalTime}")

                try {
                    sheet.addCell(dateLabel)
                    sheet.addCell(checkInLabel)
                    sheet.addCell(checkOutLabel)
                    sheet.addCell(locationLabel)
                    sheet.addCell(totalTimeLabel)
                } catch (e: WriteException) {
                    e.printStackTrace()
                }

                row++
            }

            // Add empty row between employees
            row++
        }

        try {
            workbook.write()
            workbook.close()
            println("Excel file saved at: $fileName")
            Toast.makeText(this,"Excel file saved at: $fileName",Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: WriteException) {
            e.printStackTrace()
        }
    }
}