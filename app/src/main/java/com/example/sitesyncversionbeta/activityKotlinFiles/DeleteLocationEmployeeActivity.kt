package com.example.sitesyncversionbeta.activityKotlinFiles

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sitesyncversionbeta.databinding.ActivityDeleteLocationEmployeeBinding

class DeleteLocationEmployeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeleteLocationEmployeeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteLocationEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonDeleteLocations.setOnClickListener {
            val intent = Intent(this, PermanentlyDeleteLocationsActivity::class.java)
            startActivity(intent)
        }

        binding.buttonDeleteEmployees.setOnClickListener {
            val intent = Intent(this, PermanentlyDeleteEmployeesActivity::class.java)
            startActivity(intent)
        }

    }
}