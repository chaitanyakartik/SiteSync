package com.example.sitesyncversionbeta.activityKotlinFiles.adminViewSpecific.locationManager

import DBHelper
import com.example.sitesyncversionbeta.helperClassFiles.LocationHelper
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sitesyncversionbeta.R
import com.example.sitesyncversionbeta.dataClassFiles.Location
import com.example.sitesyncversionbeta.databinding.ActivityAddLocationBinding
import com.google.firebase.database.FirebaseDatabase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddLocationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_location)

        val dbHelper = DBHelper()
        val database = FirebaseDatabase.getInstance()

        var latitudeInput = 0.0
        var longitudeInput = 0.0

        binding = ActivityAddLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val locationHelper = LocationHelper(this)

        binding.buttonGetCoordinates.setOnClickListener{
            locationHelper.getCurrentLocation { location ->
                location?.let { (latitude, longitude,accuracy) ->
                    // Now you have latitude and longitude, you can use them directly

                    latitudeInput = latitude
                    longitudeInput = longitude

                }
            }

            binding.textViewLatitude.text = latitudeInput.toString()
            binding.textViewLongitude.text = longitudeInput.toString()
        }

        binding.buttonAddLocation.setOnClickListener {

            val locationsRef = database.getReference("locations")
            val newLocationRef = locationsRef.push()
            val newLocation = Location(nickname=binding.editTextNickname.text.toString(),
                locationId = newLocationRef.key!!,
                address=binding.editTextAddress.text.toString(),
                latitude=binding.textViewLatitude.text.toString().toDouble(),
                longitude=binding.textViewLongitude.text.toString().toDouble(),
                active=true,
                pincode=binding.editTextPincode.text.toString())

            CoroutineScope(Dispatchers.Main).launch{
                dbHelper.addLocation(newLocation,this@AddLocationActivity)
            }
        }
    }
}