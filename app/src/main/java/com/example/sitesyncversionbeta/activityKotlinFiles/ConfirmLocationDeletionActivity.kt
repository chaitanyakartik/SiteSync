package com.example.sitesyncversionbeta.activityKotlinFiles

import DBHelper
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sitesyncversionbeta.R
import com.example.sitesyncversionbeta.dataClassFiles.Location
import com.example.sitesyncversionbeta.databinding.ActivityConfirmLocationDeletionBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConfirmLocationDeletionActivity : AppCompatActivity() {
    private lateinit var  binding: ActivityConfirmLocationDeletionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_location_deletion)

        binding = ActivityConfirmLocationDeletionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val location = intent.getSerializableExtra("location") as Location

        binding.textViewAddress.text = location.address
        binding.textViewNickName.text = location.nickname
        binding.textViewPincode.text = location.pincode

        val dbHelper = DBHelper()

        binding.buttonConfirmDeletion.setOnClickListener {

            CoroutineScope(Dispatchers.Main).launch {
                dbHelper.deleteLocation(location.locationId, this@ConfirmLocationDeletionActivity)
            }

        }
    }
}