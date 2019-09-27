package com.example.smack.controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.smack.R
import com.example.smack.services.AuthService
import com.example.smack.services.UserDataService
import com.example.smack.utils.USER_DATA_CHANGE_BROADCAST
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_nav_menu)

        // Receive user data broadcast
        LocalBroadcastManager.getInstance(this).registerReceiver(
            userDataChangeReceiver, IntentFilter(
                USER_DATA_CHANGE_BROADCAST
            )
        )

        //Find a drawer layout
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        //Set toggle action bar to "Open" and "Close" action
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.nav_drawer_open,
            R.string.nav_drawer_close
        )
        //Add toggle as a listener for drawer layout
        drawerLayout.addDrawerListener(toggle)
        //Set sync state action bar
        toggle.syncState()


        //Add click listener on the login button
        login_button_nav.setOnClickListener {
            Unit
            //Set intent to move on LoginActivity
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
        //Add click listener on the add channel button
        channel_add_button_nav.setOnClickListener {
            Unit
            Toast.makeText(this, "Add channel clicked", Toast.LENGTH_SHORT).show()
        }
        //Add click listener on the send text on channel button
        send_message_button.setOnClickListener {
            Unit
            Toast.makeText(this, "Send message clicked", Toast.LENGTH_SHORT).show()
        }

    }

    // Create user data change receiver
    private val userDataChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (AuthService.isLogIn) {
                user_email.text = UserDataService.email
                Log.v("UserEmail", "User email is: ${UserDataService.email}")
                user_name.text = UserDataService.name
                val resourceId = resources.getIdentifier(UserDataService.avatarTitle, "drawable", packageName )
                Log.v("UserAvatarTitle", "User avatar title is: ${UserDataService.avatarTitle}")
                user_image_nav.setImageResource(resourceId)
                //TODO try to fix generate bk color
                //user_image_nav.setBackgroundColor(UserDataService.returnAvatarColor(UserDataService.avatarColor))
                login_button_nav.text = "Logout"
            }
        }
    }
}
