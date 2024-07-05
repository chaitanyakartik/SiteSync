import android.content.Context
import android.widget.Toast
import com.example.sitesyncversionbeta.dataClassFiles.AttendanceEntry
import com.example.sitesyncversionbeta.dataClassFiles.Employee
import com.example.sitesyncversionbeta.dataClassFiles.Location
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import android.util.Log



class DBHelper {

    private val database: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().reference
    }

    // Retrieve All employees
    suspend fun retrieveEmployees(): List<Employee> = withContext(Dispatchers.IO) {
        val employeesRef = database.child("employees")
        val employeeList = mutableListOf<Employee>()

        try {
            val snapshot = employeesRef.get().await()
            for (employeeSnapshot in snapshot.children) {
                val employee = employeeSnapshot.getValue(Employee::class.java)
                employee?.let {
                    employeeList.add(it)
                }
            }
        } catch (e: Exception) {
            // Handle database error
            println("Database error: ${e.message}")
        }

        return@withContext employeeList
    }

    suspend fun retrieveEmployeeDetailsByAuthUID(authUID: String): Employee? = withContext(Dispatchers.IO) {
        var employee: Employee? = null

        try {
            val snapshot = database.child("employees").orderByChild("authUID").equalTo(authUID).get().await()
            if (snapshot.exists()) {
                for (employeeSnapshot in snapshot.children) {
                    employee = employeeSnapshot.getValue(Employee::class.java)
                    // Assuming there's only one employee per authUID, break loop after finding one
                    break
                }
            } else {
                println("No employee found with authUID: $authUID")
            }
        } catch (e: Exception) {
            // Handle database error
            println("Database error: ${e.message}")
        }

        return@withContext employee
    }

    // Retrieve Employee Attendance by Employee ID
    suspend fun retrieveEmployeeAttendance(employeeId: String): List<AttendanceEntry> = withContext(Dispatchers.IO) {
        val attendanceRef = database.child("employees").child(employeeId).child("attendanceEntries")
        val attendanceList = mutableListOf<AttendanceEntry>()

        try {
            val snapshot = attendanceRef.get().await()
            for (attendanceSnapshot in snapshot.children) {
                val attendance = attendanceSnapshot.getValue(AttendanceEntry::class.java)
                attendance?.let {
                    attendanceList.add(it)
                }
            }
        } catch (e: Exception) {
            // Handle database error
            println("Database error: ${e.message}")
        }

        return@withContext attendanceList
    }

    suspend fun retrieveAssignedLocationsIds(employeeId: String): List<String> = withContext(Dispatchers.IO) {
        val employeeRef = database.child("employees").child(employeeId)
        val assignedLocations = mutableListOf<String>()

        try {
            val snapshot = employeeRef.child("assignedLocations").get().await()
            for (locationSnapshot in snapshot.children) {
                val locationId = locationSnapshot.getValue(String::class.java)
                locationId?.let {
                    assignedLocations.add(it)
                }
            }
        } catch (e: Exception) {
            // Handle database error
            println("Database error: ${e.message}")
        }

        return@withContext assignedLocations
    }

    suspend fun retrieveLocations(locationIds: List<String>): List<Location> = withContext(Dispatchers.IO) {
        val locationsRef = database.child("locations")
        val locationsList = mutableListOf<Location>()

        try {
            for (locationId in locationIds) {
                val locationSnapshot = locationsRef.child(locationId).get().await()
                val location = locationSnapshot.getValue(Location::class.java)
                location?.let {
                    locationsList.add(it)
                }
            }
        } catch (e: Exception) {
            // Handle database error
            println("Database error: ${e.message}")
        }

        return@withContext locationsList
    }

    // Retrieve all Locations
    suspend fun retrieveAllLocations(): List<Location> = withContext(Dispatchers.IO) {
        val attendanceRef = database.child("locations")
        val locationList = mutableListOf<Location>()

        try {
            val snapshot = attendanceRef.get().await()
            for (attendanceSnapshot in snapshot.children) {
                val location = attendanceSnapshot.getValue(Location::class.java)
                location?.let {
                    locationList.add(it)
                }
            }
        } catch (e: Exception) {
            // Handle database error
            println("Database error: ${e.message}")
        }
        return@withContext locationList
    }

    fun addLocation(location: Location, context : Context) {
        val locationRef = database.child("locations").child(location.locationId)
        val locationData = hashMapOf(
            "locationId" to location.locationId,
            "address" to location.address,
            "latitude" to location.latitude,
            "longitude" to location.longitude,
            "pincode" to location.pincode,
            "nickname" to location.nickname,
            "active" to location.active
        )
        locationRef.setValue(locationData)
            .addOnSuccessListener {
                Toast.makeText(context, "Successfully added location", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                // Handle failure
                // For example, log the error message
                println("Failed to add location: $exception")
            }
    }

    fun editLocation(location: Location, context: Context) {
        val locationRef = database.child("locations").child(location.locationId)

        val locationData = mapOf(
            "address" to location.address,
            "latitude" to location.latitude,
            "longitude" to location.longitude,
            "pincode" to location.pincode,
            "nickname" to location.nickname,
            "active" to location.active
        )

        locationRef.setValue(locationData)
            .addOnSuccessListener {
                Toast.makeText(context, "Successfully edited location", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                // Handle failure
                // For example, log the error message
                println("Failed to edit location: $exception")
            }
    }

    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371.0 // Mean radius of Earth in kilometers

        // Convert latitude and longitude from degrees to radians
        val lat1Rad = Math.toRadians(lat1)
        val lon1Rad = Math.toRadians(lon1)
        val lat2Rad = Math.toRadians(lat2)
        val lon2Rad = Math.toRadians(lon2)

        // Calculate differences in latitude and longitude
        val deltaLat = lat2Rad - lat1Rad
        val deltaLon = lon2Rad - lon1Rad

        // Apply Haversine formula
        val a = sin(deltaLat / 2).pow(2) + cos(lat1Rad) * cos(lat2Rad) * sin(deltaLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        // Calculate distance
        val distance = earthRadius * c

        return distance
    }

    suspend fun logAttendance(employeeID: String, locationNickname: String, locationLatitude: Double, locationLongitude: Double): String {
        // Generate an automatic ID for the attendance entry
        val attendanceRef = database.child("employees").child(employeeID).child("attendanceEntries").push()
        val attendanceId = attendanceRef.key ?: ""

        // Get current date and time
        val currentDate = getCurrentDate()
        val currentTime = getCurrentTime()

        // Create the attendance entry
        val attendanceEntry = AttendanceEntry(
            id = attendanceId,
            employeeID = employeeID,
            locationNickname = locationNickname,
            date = currentDate,
            checkinTime = currentTime,
            checkoutTime = "",
            locationLatitude = locationLatitude,
            locationLongitude = locationLongitude
        )

        // Log the attendance entry to the database
        attendanceRef.setValue(attendanceEntry).await()

        return attendanceId
    }

    suspend fun updateCheckoutTime(employeeId: String, attendanceEntryId: String, newCheckoutTime: String, totalTime: String) {
        try {
            // Construct the path to the checkoutTime attribute of the specified attendance entry
            val path1 = "employees/$employeeId/attendanceEntries/$attendanceEntryId/checkoutTime"
            val path2 = "employees/$employeeId/attendanceEntries/$attendanceEntryId/totalTime"

            // Update the checkoutTime attribute in the database
            database.child(path1).setValue(newCheckoutTime).await()
            database.child(path2).setValue(totalTime).await()

            // Log success or handle any other post-update actions
        } catch (e: Exception) {
            // Handle the database update error
            println("Database error: ${e.message}")
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun getCurrentTime(): String {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return timeFormat.format(Date())
    }

    // _________________________________________________________________________________________________________________________________________________


    fun addNewUser(employee: Employee, context : Context, password: String) {
        val auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        val usersRef: DatabaseReference = database.getReference("employees")

        // Generate employee ID using push method
        val newEmployeeRef = usersRef.push()
        val employeeId = newEmployeeRef.key

        // Register user in Firebase Authentication system
        auth.createUserWithEmailAndPassword(employee.email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Get auth UID
                    val authUID = task.result?.user?.uid

                    // Update employee object with generated employee ID and auth UID
                    val updatedEmployee = employee.copy(employeeId = employeeId ?: "", authUID = authUID ?: "")

                    // Save employee details to the Firebase Realtime Database
                    newEmployeeRef.setValue(updatedEmployee)
                        .addOnCompleteListener { databaseTask ->
                            if (databaseTask.isSuccessful) {
                                // Update user profile with name
                                val userProfileChangeRequest = UserProfileChangeRequest.Builder()
                                    .setDisplayName(employee.name)
                                    .build()
                                task.result?.user?.updateProfile(userProfileChangeRequest)
                                Toast.makeText(context, "User Added Successfully", Toast.LENGTH_LONG).show()
                            } else {
                                // Handle database error
                                println("Failed to add employee to database: ${databaseTask.exception}")
                            }
                        }
                } else {
                    // Handle authentication error
                    Toast.makeText(context, "Email already in use", Toast.LENGTH_SHORT).show()
                    println("Failed to register user: ${task.exception}")
                }
            }
    }

    fun editEmployee(email: String, newName: String, newNumber: String, newAssignedLocations: List<String>,context: Context): Boolean {
        // Retrieve the employee node using email
        val employeeRef = database.child("employees").orderByChild("email").equalTo(email)

        // Perform the edit operation if the employee with the given email exists
        employeeRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (employeeSnapshot in dataSnapshot.children) {
                        val employeeId = employeeSnapshot.key
                        if (employeeId != null) {
                            // Update employee details
                            employeeSnapshot.ref.child("name").setValue(newName)
                            employeeSnapshot.ref.child("number").setValue(newNumber)
                            // Update assigned locations
                            employeeSnapshot.ref.child("assignedLocations").setValue(newAssignedLocations)

                            Toast.makeText(context, "User details edited successfully", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    // Employee with the given email does not exist
                    // Handle accordingly (e.g., show error message)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
            }
        })
        return true // Assume success, actual success/failure will be determined asynchronously
    }

    fun updateUserProfile(id: String, newName: String, newNumber: String,context: Context) {
        // Get reference to the user node in the database
        val userRef = database.child("employees").child(id)

        // Update user details
        userRef.child("name").setValue(newName)
        userRef.child("number").setValue(newNumber)
        Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
    }

    fun resetPassword(email: String,context: Context) {
        val auth = FirebaseAuth.getInstance()

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Password reset email sent successfully
                    Toast.makeText(context,"Password Reset Mail sent to your email", Toast.LENGTH_LONG).show()
                } else {
                    // Password reset failed
                    Toast.makeText(context,"Email not found",Toast.LENGTH_LONG).show()
                }
            }
    }

    //=========================================================================================================================

//    suspend fun deleteEmployee(employeeId: String, context: Context) = withContext(Dispatchers.IO) {
//        val employeeRef = Firebase.database.reference.child("employees").child(employeeId)
//        val auth = FirebaseAuth.getInstance()
//
//        try {
//            // Retrieve employee data
//            val employeeSnapshot = employeeRef.get().await()
//            val authUID = employeeSnapshot.child("authUID").getValue(String::class.java)
//
//            // Remove user data from Realtime Database
//            employeeRef.removeValue().await()
//
//            // Delete user from Firebase Authentication
//            authUID?.let {
//                auth.deleteUser(it).await()
//            }
//
//            withContext(Dispatchers.Main) {
//                Toast.makeText(context, "Employee deleted successfully", Toast.LENGTH_SHORT).show()
//            }
//        } catch (e: FirebaseAuthException) {
//            // Handle Firebase Auth errors
//            println("Failed to delete employee from Auth: ${e.message}")
//            withContext(Dispatchers.Main) {
//                Toast.makeText(context, "Failed to delete employee from Auth", Toast.LENGTH_SHORT).show()
//            }
//        } catch (e: Exception) {
//            // Handle other errors
//            println("Failed to delete employee: ${e.message}")
//            withContext(Dispatchers.Main) {
//                Toast.makeText(context, "Failed to delete employee", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    suspend fun deleteEmployee(employeeId: String, authUID: String, employeeEmail:String, password: String, context: Context) = withContext(Dispatchers.IO) {
        val employeeRef = database.child("employees").child(employeeId)

        try {
            // First, delete from Realtime Database

            // Second, delete from Firebase Authentication
            FirebaseAuth.getInstance().signInWithEmailAndPassword(employeeEmail, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Get the FirebaseUser
                        val user = FirebaseAuth.getInstance().currentUser

                        // Check if the user exists and delete
                        if (user != null && user.uid == authUID) {
                            user.delete()
                                .addOnSuccessListener {
                                    // User deleted from Authentication successfully
                                    Toast.makeText(context, "Employee deleted successfully", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    // Handle failure to delete from Authentication
                                    println("Failed to delete user from Authentication: ${e.message}")
                                    Log.d("CHECK THIS", "Failed to delete user from Authentication: ${e.message}")
                                    Toast.makeText(context, "Failed to delete employee", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            // User not found or UID mismatch
                            Toast.makeText(context, "User not found or UID mismatch", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Authentication sign-in failed
                        Log.d("CHECK THIS", "Failed to sign in to delete user: ${task.exception?.message}")
                        println("Failed to sign in to delete user: ${task.exception?.message}")
                        Toast.makeText(context, "Failed to delete employee", Toast.LENGTH_SHORT).show()
                    }
                }
//in the last remove employee value from realtime database
            employeeRef.removeValue().await()

        } catch (e: Exception) {
            // Handle database error
            println("Failed to delete employee: ${e.message}")
            Toast.makeText(context, "Failed to delete employee", Toast.LENGTH_SHORT).show()
        }
    }


    suspend fun deleteLocation(locationId: String, context: Context) = withContext(Dispatchers.IO) {
        val locationRef = database.child("locations").child(locationId)

        try {
            locationRef.removeValue().await()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Location deleted successfully", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            // Handle database error
            println("Failed to delete location: ${e.message}")
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Failed to delete location", Toast.LENGTH_SHORT).show()
            }
        }
    }


}
