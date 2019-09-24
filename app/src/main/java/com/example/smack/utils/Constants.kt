package com.example.smack.utils

// For heroku access
//const val BASE_URL = "https://api-for-chat.herokuapp.com/v1/"
// For local access
const val BASE_URL = "http://192.168.42.211:3005/v1/"
const val URL_REGISTER = "${BASE_URL}account/register"
const val URL_LOGIN = "${BASE_URL}account/login"
const val URL_CREATE_USER = "${BASE_URL}user/add"

//For local broadcasting
const val USER_DATA_CHANGE_BROADCAST = "USER_DATA_CHANGE_BROADCAST"