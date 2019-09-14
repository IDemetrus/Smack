package com.example.smack

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import kotlinx.android.synthetic.main.activity_user.*
import kotlin.random.Random.Default.nextInt

class UserActivity : AppCompatActivity() {

    private var avatarColor = "[0.5, 0.5, 0.5, 1]"
    private var userAvatar = "profileDefault"

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
}
