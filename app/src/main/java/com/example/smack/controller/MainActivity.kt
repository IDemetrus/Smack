package com.example.smack.controller

import android.annotation.SuppressLint
import android.content.*
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smack.R
import com.example.smack.adapters.MessageAdapter
import com.example.smack.models.Channel
import com.example.smack.models.Message
import com.example.smack.services.AuthService
import com.example.smack.services.MessageService
import com.example.smack.services.MessageService.messages
import com.example.smack.services.UserDataService
import com.example.smack.utils.SOCKET_URL
import com.example.smack.utils.USER_DATA_CHANGE_BROADCAST
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import java.lang.Integer.parseInt

class MainActivity : AppCompatActivity() {

    // Create a socket url
    private val socket = IO.socket(SOCKET_URL)

    //Create a channel Adapter
    lateinit var channelAdapter: ArrayAdapter<Channel>

    // Set channel we choose
    private var selectedChannel: Channel? = null

    //Create a message Adapter
    private lateinit var messageAdapter: MessageAdapter

    // Create adapter for channels list
    private fun setupAdapter() {
        channelAdapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, MessageService.channels)
        channel_list.adapter = channelAdapter

        messageAdapter = MessageAdapter(this, messages)
        message_list_view.adapter = this.messageAdapter
        val layoutManager = LinearLayoutManager(this)
        message_list_view.layoutManager = layoutManager
    }

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_nav_menu)


        if (App.prefs.isLogIn) {
            AuthService.findUBE(this) {}
        }


        // Connect to socket url
        socket.connect()
        socket.on("channelCreated", onNewChannel)
        socket.on("messageCreated", onNewMessage)

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

        channel_list.setOnItemClickListener { _, _, position, _ ->
            selectedChannel = MessageService.channels[position]

            drawerLayout.closeDrawer(GravityCompat.START)
            updateChannel()
        }
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
                    .setPositiveButton("Add") { _: DialogInterface?, _: Int ->
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
                    .setNegativeButton("Cancel") { _: DialogInterface?, _: Int ->
                    }
                    .show()
            } else {
                Toast.makeText(this, "You are not logged in!", Toast.LENGTH_SHORT).show()
            }
        }
        //Add click listener on the send text on channel button
        send_message_button.setOnClickListener {
            Unit
            if (App.prefs.isLogIn && message_text_field.text.isNotEmpty() && selectedChannel != null) {
                val addMessageText = this.findViewById<EditText>(R.id.message_text_field)
                val msgBody = addMessageText.text.toString()

                socket.emit(
                    "newMessage",
                    msgBody,
                    UserDataService.id,
                    selectedChannel!!.id,
                    UserDataService.name,
                    UserDataService.avatarTitle,
                    UserDataService.avatarColor
                )
                Toast.makeText(this, "Message sent!", Toast.LENGTH_SHORT).show()
                addMessageText.text.clear()
            }
            hideKeybord()
        }
    }

    // Create user data change receiver
    private val userDataChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (App.prefs.isLogIn) {
                user_email.text = UserDataService.email
                msg_user_name.text = UserDataService.name
                val resourceId =
                    resources.getIdentifier(UserDataService.avatarTitle, "drawable", packageName)
                if (resourceId > 0) {
                    user_image_nav.setImageResource(resourceId)
                }
                login_button_nav.text = "Logout"
                MessageService.getChannels { complete ->
                    if (complete) {
                        if (MessageService.channels.count() > 0) {
                            selectedChannel = MessageService.channels[0]
                            channelAdapter.notifyDataSetChanged()
                            updateChannel()
                        }
                    }
                }
            }
        }
    }

    private val onNewChannel = Emitter.Listener { args ->
        if (App.prefs.isLogIn) {
            runOnUiThread {
                val channelName = args[0] as String
                val channelDesc = args[1] as String
                val channelId = args[2] as String
                val newChannel = Channel(channelName, channelDesc, channelId)
                MessageService.channels.add(newChannel)
                channelAdapter.notifyDataSetChanged()
            }
        }

    }
    private val onNewMessage = Emitter.Listener { args ->
        if (App.prefs.isLogIn) {
            runOnUiThread {
                val channelId = args[2] as String
                if (channelId == selectedChannel?.id) {
                    val messageBody = args[0] as String
                    val userId = args[1] as String
                    val userName = args[3] as String
                    val userAvatar = args[4] as String
                    val userAvatarColor = args[5] as String
                    val newMessage =
                        Message(
                            messageBody,
                            userId,
                            channelId,
                            userName,
                            userAvatar,
                            userAvatarColor
                        )
                    messages.add(newMessage)
                    messageAdapter.notifyDataSetChanged()
                    message_list_view.smoothScrollToPosition(messageAdapter.itemCount - 1)
                }
            }
        }

    }

    // Update current channel messages
    @SuppressLint("SetTextI18n")
    fun updateChannel() {
        // Set on channel clicked then update messages chanel
        main_channel_name.text = "#${selectedChannel?.name}"
        // Download messages
        if (selectedChannel != null) {
            MessageService.getMessages(selectedChannel!!.id) { complete ->
                if (complete) {
                    messageAdapter.notifyDataSetChanged()
                    if (messageAdapter.itemCount > 0) {
                        message_list_view.smoothScrollToPosition(messageAdapter.itemCount - 1)
                    }
                }

            }
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

    fun hideKeybord() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }
}
