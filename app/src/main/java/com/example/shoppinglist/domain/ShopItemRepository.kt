package com.example.shoppinglist.domain

import androidx.lifecycle.LiveData

interface ShopItemRepository {
    fun addShopItem(shopItem: ShopItem)
    fun deleteShopItem(shopItem: ShopItem)
    fun editShopItem(shopItem: ShopItem)
    fun getShopItemById(id: Int): ShopItem
    fun getShopItems(): LiveData<List<ShopItem>>
}