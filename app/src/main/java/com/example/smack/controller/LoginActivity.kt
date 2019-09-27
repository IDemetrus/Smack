package com.example.smack.controller

import android.content.Context
import android.content.Intent
import android.hardware.input.InputManager
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import com.example.smack.R
import com.example.smack.services.AuthService
import com.example.smack.services.UserDataService
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_user.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginProgressBar.visibility = View.INVISIBLE

        //Add click listener on login button
        login_button.setOnClickListener {
            val userEmail = login_email_text.text.toString().trim()
            val userPassword = login_password_text.text.toString().trim()
            // Hide keyboard after complete input
            hideKeybord()
            Unit
            if (userEmail.isNotEmpty() && userPassword.isNotEmpty()) {
                AuthService.loginUser(this, userEmail, userPassword) { complete ->
                    enableSpinner(false)
                    if (complete) {
                        Toast.makeText(
                            this,
                            "Token received as: ${AuthService.authToken}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        AuthService.findUBE(this) { complete ->
                            if (complete) {
                                Toast.makeText(
                                    this,
                                    "Successfully authorized as: $userEmail",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Toast.makeText(
                                    this,
                                    "Email received as: ${UserDataService.email}",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                Toast.makeText(
                                    this,
                                    "Name received as: ${UserDataService.name}",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()

                                finish()
                            } else {
                                Toast.makeText(
                                    this,
                                    "Could not auth as: $userEmail",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }

                    } else {
                        Toast.makeText(this, "Could not log in as: $userEmail", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

        }
        //Add click listener on create button
        sign_up_button.setOnClickListener {
            Unit
            //Set intent to move on UserActivity
            val intentCreateUser = Intent(this, UserActivity::class.java)
            startActivity(intentCreateUser)
        }
    }
    fun hideKeybord(){
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }
    fun enableSpinner(enable: Boolean) {
        if (enable) {
            loginProgressBar.visibility = View.VISIBLE

        } else {
            loginProgressBar.visibility = View.INVISIBLE
        }
    }

}
