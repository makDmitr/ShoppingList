package com.example.shoppinglist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class ShopItemsAdapter : RecyclerView.Adapter<ShopItemsAdapter.ShopItemViewHolder>() {

    companion object {
        const val TAG = "ShopItemsAdapter"
        private var count = 0

        const val ACTIVE_VIEW_TYPE = 1
        const val NOT_ACTIVE_VIEW_TYPE = 0

        const val MAX_POOL_SIZE = 10
    }

    var shopItems = listOf<ShopItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        Log.d(TAG, "onCreateViewHolder: ${++count}")
        val layoutId = when (viewType) {
            ACTIVE_VIEW_TYPE -> {
                R.layout.active_item
            }
            NOT_ACTIVE_VIEW_TYPE -> {
                R.layout.not_active_item
            }
            else -> {
                throw java.lang.RuntimeException("Unknown viewType = $viewType")
            }
        }

        return ShopItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                layoutId,
                parent,
                false
            )
        )
    }

    override fun getItemViewType(position: Int): Int {
        val itemToShow = shopItems[position]
        return if (itemToShow.isActive) {
            ACTIVE_VIEW_TYPE
        } else {
            NOT_ACTIVE_VIEW_TYPE
        }
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val itemToShow = shopItems[position]
        holder.bindViews(itemToShow)
    }

    override fun getItemCount(): Int {
        return shopItems.size
    }

    class ShopItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val tvItemName = view.findViewById<TextView>(R.id.tvItemName)
        val tvItemQuantity = view.findViewById<TextView>(R.id.tvItemQuantity)

        fun bindViews(itemToShow: ShopItem) {
            tvItemName.text = itemToShow.name
            tvItemQuantity.text = itemToShow.quantity.toString()


        }
    }
}