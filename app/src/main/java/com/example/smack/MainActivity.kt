package com.example.smack

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_nav_menu)

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
            Toast.makeText(this, "Login clicked", Toast.LENGTH_SHORT).show()
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
}
