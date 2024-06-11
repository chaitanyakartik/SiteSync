package redundantCode

import android.content.Context
import android.widget.Toast
import com.example.sitesyncversionbeta.dataClassFiles.Employee
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RandomRedundantCode {

//    fun addNewUser(employee: Employee, context : Context, password: String) {
//        val auth = FirebaseAuth.getInstance()
//        val database = FirebaseDatabase.getInstance()
//        val usersRef: DatabaseReference = database.getReference("employees")
//
//        // Generate employee ID using push method
//        val newEmployeeRef = usersRef.push()
//        val employeeId = newEmployeeRef.key
//
//        // Register user in Firebase Authentication system
//        auth.createUserWithEmailAndPassword(employee.email, password)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    // Get auth UID
//                    val authUID = task.result?.user?.uid
//
//                    // Update employee object with generated employee ID and auth UID
//                    val updatedEmployee = employee.copy(employeeId = employeeId ?: "", authUID = authUID ?: "")
//
//                    // Save employee details to the Firebase Realtime Database
//                    newEmployeeRef.setValue(updatedEmployee)
//                        .addOnCompleteListener { databaseTask ->
//                            if (databaseTask.isSuccessful) {
//                                // Update user profile with name
//                                val userProfileChangeRequest = UserProfileChangeRequest.Builder()
//                                    .setDisplayName(employee.name)
//                                    .build()
//                                task.result?.user?.updateProfile(userProfileChangeRequest)
//                                Toast.makeText(context, "User Added Successfully", Toast.LENGTH_LONG).show()
//                            } else {
//                                // Handle database error
//                                println("Failed to add employee to database: ${databaseTask.exception}")
//                            }
//                        }
//                } else {
//                    // Handle authentication error
//                    Toast.makeText(context, "Email already in use", Toast.LENGTH_SHORT).show()
//                    println("Failed to register user: ${task.exception}")
//                }
//            }
//    }


//    fun addNewUser(employee: Employee, context: Context) {
//        val auth = FirebaseAuth.getInstance()
//        val database = FirebaseDatabase.getInstance()
//        val usersRef: DatabaseReference = database.getReference("employees")
//
//        // Generate employee ID using push method
//        val newEmployeeRef = usersRef.push()
//        val employeeId = newEmployeeRef.key
//
//        // Get the UID of the currently logged-in user
//        val currentUser = auth.currentUser
//        val authUID = currentUser?.uid
//
//        // Update employee object with generated employee ID and auth UID
//        val updatedEmployee = employee.copy(employeeId = employeeId ?: "", authUID = authUID ?: "")
//
//        // Save employee details to the Firebase Realtime Database
//        newEmployeeRef.setValue(updatedEmployee)
//            .addOnCompleteListener { databaseTask ->
//                if (databaseTask.isSuccessful) {
//                    // Update user profile with name (optional)
//                    val userProfileChangeRequest = UserProfileChangeRequest.Builder()
//                        .setDisplayName(employee.name)
//                        .build()
//                    currentUser?.updateProfile(userProfileChangeRequest)
//
//                    Toast.makeText(context, "User Added Successfully", Toast.LENGTH_LONG).show()
//                } else {
//                    // Handle database error
//                    Toast.makeText(context, "Failed to add employee to database: ${databaseTask.exception}", Toast.LENGTH_SHORT).show()
//                }
//            }
//    }

//    suspend fun retrievePermissibleDistance(): Double = withContext(Dispatchers.IO){
//        val valueRef = database.child("miscelleneious").child("permissibleDistance")
//        var value = 0.0
//
//        try {
//            val snapshot = valueRef.get().await()
//            value = snapshot.getValue(Double::class.java)!!
//        } catch (e: Exception) {
//            // Handle database error
//            println("Database error: ${e.message}")
//        }
//
//        return@withContext value
//    }
}