package com.example.sitesyncversionbeta.dataClassFiles

import java.io.Serializable

data class AttendanceEntry(
    val id: String = "",
    val employeeID: String = "",
    val locationNickname: String = "",
    val date: String = "",
    val checkinTime: String = "",
    val checkoutTime: String = "",
    val totalTime: String="",
    val locationLatitude: Double=0.0,
    val locationLongitude: Double=0.0
) : Serializable
