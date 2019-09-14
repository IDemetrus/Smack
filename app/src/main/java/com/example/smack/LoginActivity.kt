package com.example.smack

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Add click listener on login button
        login_button.setOnClickListener {
            Unit
            Toast.makeText(this, "Login clicked", Toast.LENGTH_SHORT).show()
        }
        //Add click listener on create button
        create_user_button.setOnClickListener {
            Unit
            Toast.makeText(this, "Create clicked", Toast.LENGTH_SHORT).show()
        }
    }
}
