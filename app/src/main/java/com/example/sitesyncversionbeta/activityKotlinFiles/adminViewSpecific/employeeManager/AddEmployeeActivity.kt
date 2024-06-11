package com.example.sitesyncversionbeta.activityKotlinFiles.adminViewSpecific.employeeManager

import DBHelper
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.sitesyncversionbeta.R
import com.example.sitesyncversionbeta.dataClassFiles.AttendanceEntry
import com.example.sitesyncversionbeta.databinding.ActivityAddEmployeeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.sitesyncversionbeta.dataClassFiles.Employee

class AddEmployeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEmployeeBinding

    private lateinit var selectedLocationsAdapter: ArrayAdapter<String>
    private val selectedLocations = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_employee)

        val dbHelper = DBHelper()

        binding = ActivityAddEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val roles = listOf("Admin", "Employee")
        val spinnerAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,roles)
        binding.spinnerRole.adapter = spinnerAdapter

        // Populate spinner with locations
        CoroutineScope(Dispatchers.Main).launch{

            val allLocations = dbHelper.retrieveAllLocations()
            val allLocationNames =  mutableListOf<String>()

            for(loc in allLocations){
                allLocationNames.add(loc.nickname)
            }

            // Populate spinner with locations
            val spinnerAdapter = ArrayAdapter(this@AddEmployeeActivity, android.R.layout.simple_spinner_dropdown_item, allLocationNames)
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerLocations.adapter = spinnerAdapter

            // Initialize adapter for selected locations list view
            selectedLocationsAdapter = ArrayAdapter(this@AddEmployeeActivity, android.R.layout.simple_list_item_1, selectedLocations)
            binding.listViewSelectedLocations.adapter = selectedLocationsAdapter


            // Add location button click listener
            binding.buttonAddLocation.setOnClickListener {
                val selectedLocation = binding.spinnerLocations.selectedItem as String
                if (!selectedLocations.contains(selectedLocation)) {
                    selectedLocations.add(selectedLocation)
                    selectedLocationsAdapter.notifyDataSetChanged()
                }
            }

            // Remove location when item in list view is clicked
            binding.listViewSelectedLocations.setOnItemClickListener { _, _, position, _ ->
                selectedLocations.removeAt(position)
                selectedLocationsAdapter.notifyDataSetChanged()
            }

            val listOfSelectedLocationIDS = mutableListOf<String>()


            //now we need to call the addEMployee functon, in which it takes Name, Number, Email, List(location nicknames) as parameters. it has to generate employeeID using push, authUID as it creates a proifle and then gets UID, profile picture later, role = employee, emplty attenacee trneus

            binding.buttonAddEmployee.setOnClickListener{

                for (loc1 in selectedLocations){
                    for (loc2 in allLocations){
                        if (loc1 == loc2.nickname){
                            listOfSelectedLocationIDS.add(loc2.locationId)
                        }
                    }
                }

                Log.d("Check this out ",listOfSelectedLocationIDS.toString())

                val employeeObject = Employee(name = binding.editTextName.text.toString(),
                    number = binding.editTextNumber.text.toString().trim(),
                    email = binding.editTextEmail.text.toString().trim(),
                    role = binding.spinnerRole.selectedItem.toString(),
                    assignedLocations = listOfSelectedLocationIDS,
                    attendanceEntries = emptyMap<String, AttendanceEntry>()
                )

                dbHelper.addNewUser(employee= employeeObject,context= this@AddEmployeeActivity,password= binding.editTextPassword.text.toString().trim())
            }

        }


    }
}