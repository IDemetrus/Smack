package com.example.smack.models

class Channel(val name: String, val desc: String, val id: String) {
    override fun toString(): String {
        return "#$name"
    }

}