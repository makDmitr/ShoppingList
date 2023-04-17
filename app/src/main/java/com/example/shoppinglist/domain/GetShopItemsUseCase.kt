package com.example.shoppinglist.domain

class GetShopItemsUseCase(private val shopItemRepository: ShopItemRepository) {
    fun getShopItems(): List<ShopItem> {
        return shopItemRepository.getShopItems()
    }
}