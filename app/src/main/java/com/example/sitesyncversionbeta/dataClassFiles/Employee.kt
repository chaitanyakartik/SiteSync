package com.example.sitesyncversionbeta.dataClassFiles

import com.example.sitesyncversionbeta.dataClassFiles.AttendanceEntry
import java.io.Serializable

data class Employee(
    val employeeId: String = "",
    val authUID: String = "",
    val name: String = "",
    val number: String = "",
    val email: String = "",
    val profilePicture: String = "",
    val role: String = "",
    val attendanceEntries: Map<String, AttendanceEntry> = emptyMap(),
    val assignedLocations: List<String> = emptyList()
) : Serializable


