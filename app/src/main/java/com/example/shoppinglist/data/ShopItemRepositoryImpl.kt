package com.example.shoppinglist.data

import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopItemRepository

class ShopItemRepositoryImpl: ShopItemRepository {

    private val shopItems = mutableListOf<ShopItem>()
    private var newItemId = 0


    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNSPECIFIED_ID) {
            shopItem.id = newItemId++
        }
        shopItems.add(shopItem)
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopItems.remove(shopItem)
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldShopItem = getShopItemById(shopItem.id)
        deleteShopItem(oldShopItem)
        addShopItem(shopItem)
    }

    override fun getShopItemById(id: Int): ShopItem {
        return shopItems.find {
            it.id == id
        } ?: throw java.lang.RuntimeException("There is no element with id=$id")
    }

    override fun getShopItems(): List<ShopItem> {
        return shopItems.toList()
    }
}