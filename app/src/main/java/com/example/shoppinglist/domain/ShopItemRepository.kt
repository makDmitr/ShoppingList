package com.example.shoppinglist.domain

interface ShopItemRepository {
    fun addShopItem(shopItem: ShopItem)
    fun deleteShopItem(shopItem: ShopItem)
    fun editShopItem(shopItem: ShopItem)
    fun getShopItemById(id: Int): ShopItem
    fun getShopItems(): List<ShopItem>
}