package com.example.sitesyncversionbeta

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sitesyncversionbeta.activityKotlinFiles.ForgotPasswordActivity
import com.example.sitesyncversionbeta.databinding.ActivityDeleteLocationEmployeeBinding
import com.example.sitesyncversionbeta.databinding.ActivityLoginPageBinding

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