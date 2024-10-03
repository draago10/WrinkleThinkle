package com.example.wrinklethinkle.view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wrinklethinkle.R


class InventoryViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    val itemImage: ImageView = view.findViewById(R.id.imageView5);
    val itemAmound: TextView = view.findViewById(R.id.textView2);
}