package com.example.sitesyncversionbeta.activityKotlinFiles.adminViewSpecific.employeeManager

import DBHelper
import android.os.Bundle
import android.text.Editable
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.sitesyncversionbeta.R
import com.example.sitesyncversionbeta.dataClassFiles.Employee
import com.example.sitesyncversionbeta.databinding.ActivityEditEmployeeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditEmployeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditEmployeeBinding
    private lateinit var selectedLocationsAdapter: ArrayAdapter<String>
    private val selectedLocationNames = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_employee)

        binding = ActivityEditEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbHelper = DBHelper()

        val employee = intent.getSerializableExtra("employee") as Employee

        binding.textViewEmail.text = employee.email
        binding.editTextName.text = Editable.Factory.getInstance().newEditable(employee.name)
        binding.editTextNumber.text = Editable.Factory.getInstance().newEditable(employee.number)

        CoroutineScope(Dispatchers.Main).launch {
            val allLocations = dbHelper.retrieveAllLocations()
            val allLocationNames = mutableListOf<String>()

            for (loc in allLocations) {
                allLocationNames.add(loc.nickname)
            }

            // Populate spinner with locations
            val spinnerAdapter = ArrayAdapter(
                this@EditEmployeeActivity,
                android.R.layout.simple_spinner_item,
                allLocationNames
            )
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerLocations.adapter = spinnerAdapter

            //get selected locations from already assigned locations
            val alreadySelectedLocationIDS = employee.assignedLocations
            for (loc1 in alreadySelectedLocationIDS) {
                for (loc2 in allLocations) {
                    if (loc1 == loc2.locationId) {
                        selectedLocationNames.add(loc2.nickname)
                    }
                }
            }

            // Initialize adapter for selected locations list view
            selectedLocationsAdapter = ArrayAdapter(
                this@EditEmployeeActivity,
                android.R.layout.simple_list_item_1,
                selectedLocationNames
            )
            binding.listViewSelectedLocations.adapter = selectedLocationsAdapter

            // Add location button click listener
            binding.buttonAddLocation.setOnClickListener {
                val selectedLocation = binding.spinnerLocations.selectedItem as String
                if (!selectedLocationNames.contains(selectedLocation)) {
                    selectedLocationNames.add(selectedLocation)
                    selectedLocationsAdapter.notifyDataSetChanged()
                }
            }

            // Remove location when item in list view is clicked
            binding.listViewSelectedLocations.setOnItemClickListener { _, _, position, _ ->
                selectedLocationNames.removeAt(position)
                selectedLocationsAdapter.notifyDataSetChanged()
            }

            //Call the editEmployee function that takes email and employees as parameter and edits it. the changes that can be done to employee are name, number, assigned locations

            val listOfSelectedLocationIDS = mutableListOf<String>()


            //now we need to call the addEMployee functon, in which it takes Name, Number, Email, List(location nicknames) as parameters. it has to generate employeeID using push, authUID as it creates a proifle and then gets UID, profile picture later, role = employee, emplty attenacee trneus

            binding.buttonEditEmployee.setOnClickListener {

                for (loc1 in selectedLocationNames) {
                    for (loc2 in allLocations) {
                        if (loc1 == loc2.nickname) {
                            listOfSelectedLocationIDS.add(loc2.locationId)
                        }
                    }
                }

                dbHelper.editEmployee(email = binding.textViewEmail.text.toString(),
                    newName = binding.editTextName.text.toString(),
                    newNumber = binding.editTextNumber.text.toString(),
                    newAssignedLocations = listOfSelectedLocationIDS,
                    context = this@EditEmployeeActivity
                )
            }



        }
    }
}