package com.example.sitesyncversionbeta.activityKotlinFiles

import DBHelper
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sitesyncversionbeta.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbHelper = DBHelper()

        binding.buttonPasswordRecover.setOnClickListener {
            // Retrieve user input
            val email = binding.editTextEmail.text.toString().trim()
            dbHelper.resetPassword(email,this)
        }
    }
}
