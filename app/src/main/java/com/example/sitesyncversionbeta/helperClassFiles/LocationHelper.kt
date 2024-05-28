package com.example.sitesyncversionbeta.helperClassFiles

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlin.properties.Delegates


class LocationHelper(private val context: Context) {

    // Request code for location permission
    private val LOCATION_PERMISSION_REQUEST_CODE = 100

    // FusedLocationProviderClient instance
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude by Delegates.notNull<Double>()
    private var longitude by Delegates.notNull<Double>()

    fun getCurrentLocation(callback: (Triple<Double, Double,Float>?) -> Unit) {
        // Check if permission is granted
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Request location permission
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            callback(null)
            return
        }

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context as Activity)

        // Get last known location asynchronously
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude
                    val accuracy = location.accuracy
                    callback(Triple(latitude, longitude,accuracy))
                } else {
                    // Location is null
                    Toast.makeText(context, "Please check if Location is on", Toast.LENGTH_SHORT).show()
                    Log.e("LocationApp", "Last known location is null")
                    callback(null)
                }
            }
            .addOnFailureListener { e ->
                // Handle failure
                Toast.makeText(context, "Please check if Location is on", Toast.LENGTH_SHORT).show()
                Log.e("LocationApp", "Error getting location", e)
                callback(null)
            }
    }
}
