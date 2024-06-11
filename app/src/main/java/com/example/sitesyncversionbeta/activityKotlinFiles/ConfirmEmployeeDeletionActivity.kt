package com.example.sitesyncversionbeta.activityKotlinFiles

import DBHelper
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sitesyncversionbeta.R
import com.example.sitesyncversionbeta.dataClassFiles.Employee
import com.example.sitesyncversionbeta.databinding.ActivityConfirmEmployeeDeletionBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConfirmEmployeeDeletionActivity : AppCompatActivity() {
    private lateinit var  binding: ActivityConfirmEmployeeDeletionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_employee_deletion)

        binding = ActivityConfirmEmployeeDeletionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val employee = intent.getSerializableExtra("employee") as Employee

        binding.textViewEmail.text = employee.email
        binding.textViewName.text = employee.name
        binding.textViewNumber.text = employee.number

        val dbHelper = DBHelper()

        binding.buttonConfirmDeletion.setOnClickListener {

            CoroutineScope(Dispatchers.Main).launch {
                dbHelper.deleteEmployee(employee.employeeId, this@ConfirmEmployeeDeletionActivity)
            }
        }
    }
}