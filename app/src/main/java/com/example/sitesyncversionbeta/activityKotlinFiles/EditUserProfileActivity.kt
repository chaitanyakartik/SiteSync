package com.example.sitesyncversionbeta.activityKotlinFiles

import DBHelper
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.sitesyncversionbeta.dataClassFiles.Employee
import com.example.sitesyncversionbeta.databinding.ActivityEditUserProfileBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditUserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditUserProfileBinding
    private lateinit var dbHelper: DBHelper

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper()

        val userDetails = intent.getSerializableExtra("UserDetails") as? Employee

        if (userDetails != null) {
            binding.editTextName.text = Editable.Factory.getInstance().newEditable(userDetails.name)
            binding.editTextNumber.text = Editable.Factory.getInstance().newEditable(userDetails.number)
            binding.buttonSave.setOnClickListener {
                val newName = binding.editTextName.text.toString()
                val newNumber = binding.editTextNumber.text.toString()
                CoroutineScope(Dispatchers.Main).launch {
                    dbHelper.updateUserProfile(newName = newName, newNumber = newNumber, id = userDetails.employeeId,context = this@EditUserProfileActivity)
                }
            }
        }
        else{
            binding.buttonSave.setOnClickListener{
                Toast.makeText(this@EditUserProfileActivity, "UserDetails Found to be null", Toast.LENGTH_SHORT).show()
            }
        }



    }
}
