package com.example.a2in1app

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_row.view.*

class MessageAdaptor(private val messages: ArrayList<String>): RecyclerView.Adapter<MessageAdaptor.ItemViewHolder>() {
    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val message = messages[position]
        holder.itemView.apply {
            tvMessage.text = message
            if (message.contains("You got it!") || message.contains("Found") ) {
                tvMessage.setTextColor(Color.GREEN)
            }
            else if (message.contains("Wrong guess") || message.contains("No")) {
                tvMessage.setTextColor(Color.RED)
            }

        }

    }

    override fun getItemCount() = messages.size
}