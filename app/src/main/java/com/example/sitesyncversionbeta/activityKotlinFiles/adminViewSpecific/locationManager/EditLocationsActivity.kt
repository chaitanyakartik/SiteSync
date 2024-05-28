package com.example.sitesyncversionbeta.activityKotlinFiles.adminViewSpecific.locationManager

import DBHelper
import com.example.sitesyncversionbeta.helperClassFiles.LocationHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import com.example.sitesyncversionbeta.R
import com.example.sitesyncversionbeta.dataClassFiles.Location
import com.example.sitesyncversionbeta.databinding.ActivityEditLocationsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditLocationsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditLocationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_locations)

        val dbHelper = DBHelper()
        val locationHelper = LocationHelper(this)

        val location = intent.getSerializableExtra("location") as Location

        binding = ActivityEditLocationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editTextNickname.text = Editable.Factory.getInstance().newEditable(location.nickname)
        binding.editTextAddress.text = Editable.Factory.getInstance().newEditable(location.address)
        binding.editTextPincode.text = Editable.Factory.getInstance().newEditable(location.pincode)

        binding.textViewLatitude.text = location.latitude.toString()
        binding.textViewLongitude.text = location.longitude.toString()

        var latitudeInput = 0.0
        var longitudeInput = 0.0

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

        binding.buttonEditLocation.setOnClickListener {

            val locationRef = location.locationId
            val newLocation = Location(nickname=binding.editTextNickname.text.toString(),
                locationId = locationRef,
                address=binding.editTextAddress.text.toString(),
                latitude=binding.textViewLatitude.text.toString().toDouble(),
                longitude=binding.textViewLongitude.text.toString().toDouble(),
                active=true,
                pincode=binding.editTextPincode.text.toString())

            CoroutineScope(Dispatchers.Main).launch{
                dbHelper.editLocation(newLocation,this@EditLocationsActivity)
            }
        }


    }
}