package com.example.smack.models

class Message(
    val messageBody: String,
    val userId: String,
    val channelId: String,
    val userName: String,
    val userAvatar: String,
    val userAvatarColor: String
) {
    override fun toString(): String {
        return messageBody
    }
}