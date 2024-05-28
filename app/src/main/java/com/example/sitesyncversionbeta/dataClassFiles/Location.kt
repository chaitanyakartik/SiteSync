package com.example.sitesyncversionbeta.dataClassFiles
import java.io.Serializable

data class Location(
    val locationId: String = "",
    val address: String = "",
    val latitude: Double=0.0,
    val longitude: Double=0.0,
    val nickname: String="",
    val active: Boolean=true,
    val pincode: String=""
): Serializable
