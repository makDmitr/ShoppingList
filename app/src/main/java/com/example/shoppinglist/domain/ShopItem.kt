package com.example.shoppinglist.domain

data class ShopItem(
    val name: String,
    val quantity: Int,
    var isActive: Boolean,
    var id: Int = UNSPECIFIED_ID
) {
    companion object {
        const val UNSPECIFIED_ID = -1
    }
}
