package com.example.smack.controller

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smack.R
import com.example.smack.services.AuthService
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Add click listener on login button
        login_button.setOnClickListener {
            val userEmail = login_email_text.text.toString().trim()
            val userPassword = login_password_text.text.toString().trim()
            // Hide keyboard after complete input
            hideKeybord()
            Unit
            if (userEmail.isNotEmpty() && userPassword.isNotEmpty()) {
                AuthService.loginUser( userEmail, userPassword) { complete ->
                    if (complete) {
                        AuthService.findUBE(this) {
                            if (it) {
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

}
