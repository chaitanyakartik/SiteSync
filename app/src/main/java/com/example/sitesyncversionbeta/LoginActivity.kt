// This activity facilitates user login through the Firebase User Authentication service

package com.example.sitesyncversionbeta

import DBHelper
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sitesyncversionbeta.activityKotlinFiles.employeeViewSpecific.MainActivity
import com.example.sitesyncversionbeta.activityKotlinFiles.adminViewSpecific.MainActivityAdminView
import com.example.sitesyncversionbeta.activityKotlinFiles.ForgotPasswordActivity
import com.example.sitesyncversionbeta.databinding.ActivityLoginPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginPageBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbHelper = DBHelper()

        //setup onclick listeners
        binding.buttonForgotPassword.setOnClickListener {
            // Navigate to registration page activity
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        binding.buttonEmployeeView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.buttonAdminView.setOnClickListener {
            val intent = Intent(this, MainActivityAdminView::class.java)
            startActivity(intent)
        }

        // Initialize Firebase Auth and Realtime Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()


        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            // Call the login function with email and password

            //LOGIN
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login success, retrieve user's UID
                    val user = auth.currentUser
                    user?.uid?.let { uid ->
                        // Retrieve user-specific data from Realtime Database using UID

                        CoroutineScope(Dispatchers.Main).launch {
                            val currentUser = dbHelper.retrieveEmployeeDetailsByAuthUID(uid)!!

                            if (currentUser.role == "Employee") {
                                val intent =  Intent(this@LoginActivity, MainActivity::class.java)
                                intent.putExtra("UserDetails", currentUser)
                                startActivity(intent)
                            }
                            else if (currentUser.role == "Admin"){
                                val intent = Intent(this@LoginActivity, MainActivityAdminView::class.java)
                                intent.putExtra("UserDetails", currentUser)
                                startActivity(intent)
                            }
                            else{
                                Log.d("Unknown ERROR","User not found")
                            }
                        }
                    }
                }
                else {
                    // Login failed
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
