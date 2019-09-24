package com.example.smack.services

import android.graphics.Color
import java.util.*

object UserDataService {
    var id = ""
    var avatarColor = ""
    var avatarTitle = ""
    var email = ""
    var name = ""
    var token = ""

    fun returnAvatarColor(componets: String): Int {
        val strippedColor = componets
            .replace("[", "")
            .replace("]", "")
            .replace(",", "")
        var r = 0
        var g = 0
        var b = 0
        val scanner = Scanner(strippedColor)
        if (scanner.hasNext()) {
            r = (scanner.nextDouble() * 255).toInt()
            g = (scanner.nextDouble() * 255).toInt()
            b = (scanner.nextDouble() * 255).toInt()
        }
        return Color.rgb(r, g, b)
    }
}