package com.example.smack.services

import android.content.Context
import android.util.JsonToken
import android.util.Log
import com.android.volley.Header
import com.android.volley.Response
import com.android.volley.Response.ErrorListener
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.smack.utils.URL_CREATE_USER
import com.example.smack.utils.URL_FIND_USER_BY_EMAIL
import com.example.smack.utils.URL_LOGIN
import com.example.smack.utils.URL_REGISTER
import org.json.JSONException
import org.json.JSONObject

object AuthService {

    var isLogIn = false
    var userEmail = ""
    var authToken = ""

    fun registerUser(
        context: Context,
        email: String,
        password: String,
        complete: (Boolean) -> Unit
    ) {
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()
        val registerRequest = object : StringRequest(Method.POST,
            URL_REGISTER,
            Response.Listener { complete(true) },
            ErrorListener { error ->
                Log.d("ERROR", "Could not register user: $error")
                complete(false)
            }) {
            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
        }
        Volley.newRequestQueue(context).add(registerRequest)
    }

    fun loginUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()
        val loginRequest = object : JsonObjectRequest(Method.POST,
            URL_LOGIN, null,
            Response.Listener { response ->
                try {
                    //here is we parse the json object
                    userEmail = response.getString("user")
                    authToken = response.getString("token")
                    isLogIn = true
                    complete(true)
                } catch (e: JSONException) {
                    Log.d("JSON", "EXC: " + e.localizedMessage)
                    complete(false)
                }

            },
            ErrorListener { error ->
                //here is we got some errors
                Log.d("ERROR", "Could not log in as user: $error")
                complete(false)
            }) {
            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
        }
        Volley.newRequestQueue(context).add(loginRequest)
    }

    fun createUser(
        context: Context,
        name: String,
        email: String,
        avatarTitle: String,
        avatarColor: String,
        complete: (Boolean) -> Unit
    ) {
        val jsonBody = JSONObject()
        jsonBody.put("name", name)
        jsonBody.put("email", email)
        jsonBody.put("avatarName", avatarTitle)
        jsonBody.put("avatarColor", avatarColor)
        val requestBody = jsonBody.toString()
        val createUserRequest = object : JsonObjectRequest(
            Method.POST,
            URL_CREATE_USER,
            null,
            Response.Listener { response ->
                try {
                    UserDataService.name = response.getString("name")
                    UserDataService.email = response.getString("email")
                    UserDataService.avatarTitle = response.getString("avatarName")
                    UserDataService.avatarColor = response.getString("avatarColor")
                    UserDataService.id = response.getString("_id")
                    complete(true)
                } catch (e: JSONException) {
                    Log.d("JSON", "EXC: " + e.localizedMessage)
                    complete(false)
                }
            },
            ErrorListener { error ->
                Log.d("ERROR", "Could not create user: $error")
                complete(false)
            }) {
            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $authToken"
                return headers
            }
        }
        Volley.newRequestQueue(context).add(createUserRequest)
    }
    fun findUserByEmail( context: Context, complete: (Boolean) -> Unit ) {
        val requestFindUserByEmail = object : JsonObjectRequest(Method.GET, URL_FIND_USER_BY_EMAIL+user Email, null, Response.Listener { response ->
            try {
                UserDataService.name = response.getString("name")
                UserDataService.email = response.getString("email")
                UserDataService.avatarTitle = response.getString("avatarName")
                UserDataService.avatarColor = response.getString("avatarColor")
                UserDataService.id = response.getString("_id")
                complete(true)
            } catch (e: JSONException) {
                Log.d("JSON", "EXC: " + e.localizedMessage)
                complete(false)
            }
        }, ErrorListener { error ->
            Log.d("ERROR", "Could not create user: $error")
            complete(false)
        }){
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = authToken
                return headers
            }
        }
        Volley.newRequestQueue(context).add(requestFindUserByEmail)
    }
}