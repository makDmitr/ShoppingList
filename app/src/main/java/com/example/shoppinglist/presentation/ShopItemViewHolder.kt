package com.example.shoppinglist.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class ShopItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    val tvItemName = view.findViewById<TextView>(R.id.tvItemName)
    val tvItemQuantity = view.findViewById<TextView>(R.id.tvItemQuantity)

    fun bindViews(
        itemToShow: ShopItem,
        onLongClickListener: ((ShopItem) -> Unit)? = null,
        onClickListener: ((ShopItem) -> Unit)? = null
    ) {
        tvItemName.text = itemToShow.name
        tvItemQuantity.text = itemToShow.quantity.toString()

        view.setOnLongClickListener {
            onLongClickListener?.invoke(itemToShow)
            true
        }

        view.setOnClickListener {
            onClickListener?.invoke(itemToShow)
        }
    }
}