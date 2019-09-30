package com.example.smack.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smack.R
import com.example.smack.models.Message

class MessageAdapter(val context: Context, val messages: ArrayList<Message>) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.messages_list_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messages.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindMessage(context, messages[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val usrName = view.findViewById<TextView>(R.id.msg_user_name)
        val usrImage = view.findViewById<ImageView>(R.id.msg_image)
        val msgBody = view.findViewById<TextView>(R.id.msg_body)

        fun bindMessage(context: Context, message: Message) {
            val resourceId =
                context.resources.getIdentifier(message.userAvatar, "drawable", context.packageName)
            usrImage.setImageResource(resourceId)
            usrName.text = message.userName
            msgBody.text = message.messageBody
        }

    }
}