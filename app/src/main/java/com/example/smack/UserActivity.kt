package com.example.smack

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        //Add click listener on create user button
        create_user_button.setOnClickListener {
            Unit
            Toast.makeText(this, "Create user clicked", Toast.LENGTH_SHORT).show()
        }
        //Add click listener on create background color button
        create_background_color_button.setOnClickListener {
            Unit
            Toast.makeText(this, "Create background color clicked", Toast.LENGTH_SHORT).show()
        }
        //Add click listener on create avatar button
        create_user_avatar_image.setOnClickListener {
            Unit
            Toast.makeText(this, "Create user avatar clicked", Toast.LENGTH_SHORT).show()
        }
    }
}
