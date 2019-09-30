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
import kotlinx.android.synthetic.main.content_main.view.*

class MessageAdapter(val context: Context, val messages: ArrayList<Message>) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
                            .inflate(R.layout.message_list_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messages.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindMessage(context, messages[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val usrImg = view.findViewById<ImageView>(R.id.msg_user_avatar)
        val usrName = view.findViewById<TextView>(R.id.msg_user_name)
        val msgBody = view.findViewById<TextView>(R.id.msg_body)

        fun bindMessage(context: Context, messages: Message) {
            val resourceId = context.resources.getIdentifier(message.userAvatar, "drawable", context.packageName)
            usrImg.setImageResource(resourceId)

            usrName.text = messages.userName
            msgBody.text = messages.messageBody
        }
    }
}