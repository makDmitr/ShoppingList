package com.example.shoppinglist.domain

class GetShopItemByIdUseCase(private val shopItemRepository: ShopItemRepository) {
    fun getShopItemById(id: Int): ShopItem {
        return shopItemRepository.getShopItemById(id)
    }
}