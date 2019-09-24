package com.example.smack.controller

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.smack.R
import com.example.smack.services.AuthService
import com.example.smack.utils.USER_DATA_CHANGE_BROADCAST
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Add click listener on login button
        login_button.setOnClickListener {
            val userEmail = login_email_text.text.toString()
            val userPassword = login_password_text.text.toString()
            Unit
            AuthService.loginUser(this, userEmail, userPassword) { complete ->
                if (complete) {
                    Toast.makeText(this, "Successfully log in as: $userEmail", Toast.LENGTH_SHORT)
                        .show()
                    // Init data login change broadcast
                    val userLoginChanged = Intent(USER_DATA_CHANGE_BROADCAST)
                    LocalBroadcastManager.getInstance(this).sendBroadcast(userLoginChanged)
//                    val intentMain = Intent(this, MainActivity::class.java)
//                    startActivity(intentMain)
                    finish()
                } else {
                    Toast.makeText(this, "Could not log in as: $userEmail", Toast.LENGTH_SHORT)
                        .show()
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

}
