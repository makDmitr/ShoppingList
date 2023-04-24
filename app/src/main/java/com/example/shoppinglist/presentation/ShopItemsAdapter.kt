package com.example.shoppinglist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class ShopItemsAdapter :
    ListAdapter<ShopItem, ShopItemViewHolder>( ShopItemDiffItemCallback() ) {

    companion object {
        const val TAG = "ShopItemsAdapter"
        private var countOnCreate = 0
        private var countOnBind = 0

        const val ACTIVE_VIEW_TYPE = 1
        const val NOT_ACTIVE_VIEW_TYPE = 0

        const val MAX_POOL_SIZE = 10
    }

    var onLongClickListener: ((ShopItem) -> Unit)? = null
    var onClickListener: ((ShopItem) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        Log.d(TAG, "onCreateViewHolder: ${++countOnCreate}")
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
        Log.d(TAG, "getItemViewType: $position")
        val itemToShow = getItem(position)
        return if (itemToShow.isActive) {
            ACTIVE_VIEW_TYPE
        } else {
            NOT_ACTIVE_VIEW_TYPE
        }
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: ${++countOnBind}")
        val itemToShow = getItem(position)
        holder.bindViews(
            itemToShow,
            onLongClickListener,
            onClickListener
        )
    }


}