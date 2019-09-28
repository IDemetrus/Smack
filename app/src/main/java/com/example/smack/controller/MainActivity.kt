package com.example.smack.controller

import android.content.*
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.smack.R
import com.example.smack.models.Channel
import com.example.smack.services.AuthService
import com.example.smack.services.MessageService
import com.example.smack.services.UserDataService
import com.example.smack.utils.SOCKET_URL
import com.example.smack.utils.USER_DATA_CHANGE_BROADCAST
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {

    // Create a socket url
    private val socket = IO.socket(SOCKET_URL)

    //Create a channel Adapter
    lateinit var channelAdapter: ArrayAdapter<Channel>

    private fun setupAdapter() {
        channelAdapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, MessageService.channels)
        channel_list.adapter = channelAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_nav_menu)

        if (App.prefs.isLogIn) {
            AuthService.findUBE(this){}
        }

        // Connect to socket url
        socket.connect()
        socket.on("channelCreated", onNewChannel)

        // Call channel adapter
        setupAdapter()

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
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.add_channel_dialog, null)
            if (App.prefs.isLogIn) {
                builder.setView(dialogView)
                    .setPositiveButton("Add") { dialog: DialogInterface?, which: Int ->
                        // Add logic then clicked
                        val addChannelName =
                            dialogView.findViewById<EditText>(R.id.channel_name_txt)
                        val addChannelDesc =
                            dialogView.findViewById<EditText>(R.id.channel_desc_txt)
                        val channelName = addChannelName.text.toString()
                        val channelDesc = addChannelDesc.text.toString()

                        // Create channel with name and description
                        socket.emit("newChannel", channelName, channelDesc)
                    }
                    .setNegativeButton("Cancel") { dialog: DialogInterface?, which: Int ->
                    }
                    .show()
            }
        }
        //Add click listener on the send text on channel button
        send_message_button.setOnClickListener {
            Unit
            Toast.makeText(this, "Send message clicked", Toast.LENGTH_SHORT).show()
        }

    }

    // Create user data change receiver
    private val userDataChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (App.prefs.isLogIn) {
                user_email.text = UserDataService.email
                user_name.text = UserDataService.name
                val resourceId =
                    resources.getIdentifier(UserDataService.avatarTitle, "drawable", packageName)
                if (resourceId > 0) {
                    user_image_nav.setImageResource(resourceId)
                }
                //TODO try to fix generate bk color
                //user_image_nav.setBackgroundColor(UserDataService.returnAvatarColor(UserDataService.avatarColor))
                login_button_nav.text = "Logout"
                MessageService.getChannels(context) { complete ->
                    if (complete) {
                        channelAdapter.notifyDataSetChanged()
                    }

                }
            }
        }
    }

    private val onNewChannel = Emitter.Listener { args ->
        runOnUiThread {
            val channelName = args[0] as String
            val channelDesc = args[1] as String
            val channelId = args[2] as String
            val newChannel = Channel(channelName, channelDesc, channelId)
            MessageService.channels.add(newChannel)
            println(newChannel.name)
            println(newChannel.desc)
            println(newChannel.id)
        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(userDataChangeReceiver)
        super.onDestroy()
        socket.disconnect()
        socket.off("channelCreated", onNewChannel)

    }

    override fun onResume() {
        // Receive user data broadcast
        LocalBroadcastManager.getInstance(this).registerReceiver(
            userDataChangeReceiver, IntentFilter(
                USER_DATA_CHANGE_BROADCAST
            )
        )
        super.onResume()
    }
}
