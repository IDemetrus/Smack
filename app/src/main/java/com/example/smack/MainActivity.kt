package com.example.smack

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout

class MainActivity : AppCompatActivity() {

    //init a drawer layout
    lateinit var drawerLayout: DrawerLayout
    //init a toggle action bar
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_nav_menu)

        //Find a drawer layout
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        //Set toggle action bar to "Open" and "Close" action
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close)
        //Add toggle as a listener for drawer layout
        drawerLayout.addDrawerListener(toggle)
        //Set sync state action bar
        toggle.syncState()
    }
}
