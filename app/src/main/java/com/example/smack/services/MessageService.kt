package com.example.smack.services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.smack.controller.App
import com.example.smack.models.Channel
import com.example.smack.utils.URL_GET_CHANNELS
import org.json.JSONException

object MessageService {
    val channels = ArrayList<Channel>()

    fun getChannels(context: Context, complete: (Boolean) -> Unit) {
        val channelRequest = object :
            JsonArrayRequest(Method.GET, URL_GET_CHANNELS, null, Response.Listener { response ->
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
}