package com.example.smack.controller

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.smack.R
import com.example.smack.services.AuthService
import com.example.smack.utils.USER_DATA_CHANGE_BROADCAST
import kotlinx.android.synthetic.main.activity_user.*
import kotlin.random.Random.Default.nextInt

class UserActivity : AppCompatActivity() {

    private var avatarColor = "[0.5, 0.5, 0.5, 1]"
    private var userAvatar = "profileDefault"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        createSpinner.visibility = View.INVISIBLE


        //Add click listener on create user button
        create_user_button.setOnClickListener {
            val userName = create_user_name_text.text.toString()
            val userEmail = create_user_email_text.text.toString()
            val userPassword = create_user_password_text.text.toString()
            Unit
            if (userName.isNotEmpty() && userEmail.isNotEmpty() && userPassword.isNotEmpty()) {
                AuthService.registerUser(this, userEmail, userPassword) { complete ->
                    if (complete) {
                        AuthService.loginUser(this, userEmail, userPassword) { complete ->
                            if (complete) {
                                Toast.makeText(
                                    this,
                                    "Successfully added new user: $userEmail",
                                    Toast.LENGTH_SHORT
                                ).show()
                                AuthService.createUser(
                                    this,
                                    userName,
                                    userEmail,
                                    userAvatar,
                                    avatarColor
                                ) { complete ->
                                    if (complete) {
                                        val userDataChanged = Intent(USER_DATA_CHANGE_BROADCAST)
                                        LocalBroadcastManager.getInstance(this)
                                            .sendBroadcast(userDataChanged)
                                        val intentMain = Intent(this, MainActivity::class.java)
                                        enableSpinner(false)
//                                        startActivity(intentMain)
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Could not register user: $userEmail",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            } else {
                                Toast.makeText(
                                    this,
                                    "Could not register user: $userEmail",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "Could not register user: $userEmail",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
        //Add click listener on create background color button
        create_background_color_button.setOnClickListener {
            Unit
            //Set random colors
            val r = nextInt(255)
            val g = nextInt(255)
            val b = nextInt(255)
            create_user_avatar_image.setBackgroundColor(Color.rgb(r, g, b))

            //Save colors for add into database later
            val savedR = r.toDouble() / 255
            val savedG = g.toDouble() / 255
            val savedB = b.toDouble() / 255
            avatarColor = "[$savedR, $savedG,$savedB, 1]"
        }
        //Add click listener on create avatar button
        create_user_avatar_image.setOnClickListener {
            Unit
            //Set an user avatar image
            val color = nextInt(2)
            val avatar = nextInt(28)
            userAvatar = if (color == 0) {
                "light$avatar"
            } else {
                "dark$avatar"
            }
            val resourceId = resources.getIdentifier(userAvatar, "drawable", packageName)
            create_user_avatar_image.setImageResource(resourceId)
        }
    }

    private fun enableSpinner(enable: Boolean) {
        if (enable) {
            createSpinner.visibility = View.VISIBLE

        } else {
            createSpinner.visibility = View.INVISIBLE
        }
        create_user_button.isEnabled = !enable
        create_user_avatar_image.isEnabled = !enable
        create_background_color_button.isEnabled = !enable
    }
}
