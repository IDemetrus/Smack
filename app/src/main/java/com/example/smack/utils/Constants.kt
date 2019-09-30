package com.example.smack.utils

// For heroku access
const val BASE_URL = "https://api-for-chat.herokuapp.com/v1/"
// For socket access
const val SOCKET_URL = "https://api-for-chat.herokuapp.com/"
// For local access Linux PC
// const val BASE_URL = "http://192.168.42.211:3005/v1/"
//const val BASE_URL = "http://10.0.2.3:3005/v1/"
// For local access Windows
//const val BASE_URL = "http://192.168.42.67:3005/v1/"
const val URL_REGISTER = "${BASE_URL}account/register"
const val URL_LOGIN = "${BASE_URL}account/login"
const val URL_CREATE_USER = "${BASE_URL}user/add"
const val URL_FIND_USER_BY_EMAIL = "${BASE_URL}user/byEmail/"
const val URL_GET_CHANNELS = "${BASE_URL}channel"


//For local broadcasting
const val USER_DATA_CHANGE_BROADCAST = "USER_DATA_CHANGE_BROADCAST"