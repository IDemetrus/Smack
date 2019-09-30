package com.example.smack.services

import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.example.smack.controller.App
import com.example.smack.models.Channel
import com.example.smack.models.Message
import com.example.smack.utils.URL_GET_CHANNELS
import com.example.smack.utils.URL_GET_MESSAGES
import org.json.JSONException

object MessageService {
    val channels = ArrayList<Channel>()
    val messages = ArrayList<Message>()

    fun getChannels(complete: (Boolean) -> Unit) {
        val channelRequest = object :
            JsonArrayRequest(Method.GET, URL_GET_CHANNELS, null, Response.Listener { response ->
                clearChannels()
                try {
                    for (i in 0 until response.length()) {
                        val channel = response.getJSONObject(i)
                        val name = channel.getString("name")
                        val desc = channel.getString("description")
                        val id = channel.getString("_id")
                        val newChannel = Channel(name, desc, id)
                        this.channels.add(newChannel)
                    }
                    complete(true)
                } catch (e: JSONException) {
                    Log.d("JSON", "EXC: " + e.localizedMessage)
                    complete(false)
                }
            }, Response.ErrorListener { error ->
                Log.d("ERROR", "Could not retrieve channels")
                complete(false)
            }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer ${App.prefs.authToken}"
                return headers
            }
        }
        App.prefs.requestQueue.add(channelRequest)
    }

    fun getMessages(channel: String?, complete: (Boolean) -> Unit) {
        val messageRequest = object : JsonArrayRequest(
            Method.GET,
            "$URL_GET_MESSAGES${channel}",
            null,
            Response.Listener { response ->
                clearMessages()
                try {
                    for (i in 0 until response.length()) {
                        val message = response.getJSONObject(i)
                        val body = message.getString("messageBody")
                        val userId = message.getString("userId")
                        val channelId = message.getString("channelId")
                        val userName = message.getString("userName")
                        val userAvatar = message.getString("userAvatar")
                        val userAvatarColor = message.getString("userAvatarColor")
                        val newMessage =
                            Message(body, userId, channelId, userName, userAvatar, userAvatarColor)
                        this.messages.add(newMessage)
                    }
                    complete(true)
                } catch (e: JSONException) {
                    Log.d("JSON", "EXC: " + e.localizedMessage)
                    complete(false)
                }
            },
            Response.ErrorListener { error ->
                Log.d("ERROR", "Could not get messages")
                complete(false)
            }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer ${App.prefs.authToken}"
                return headers
            }
        }
        App.prefs.requestQueue.add(messageRequest)
    }
    fun clearMessages() {
        messages.clear()
    }

    fun clearChannels() {
        channels.clear()
    }
}