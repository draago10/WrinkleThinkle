package com.example.wrinklethinkle.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wrinklethinkle.R


class InventoryViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    val itemImage: ImageView = view.findViewById(R.id.imageView5);
    val itemAmount: TextView = view.findViewById(R.id.textView2);
}

class InventoryAdapter(val inventory:List<String>) : RecyclerView.Adapter<InventoryViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): InventoryViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.inventory_custom_cell, p0, false);
        return InventoryViewHolder(view)
    }

    override fun onBindViewHolder(p0: InventoryViewHolder, p1: Int) {
        val item = inventory[p1];
        p0.itemAmount.text = item
//        p0.itemImage.setImageResource(item.imageResId)
    }
    override fun getItemCount(): Int {
        return inventory.size
    }
}

