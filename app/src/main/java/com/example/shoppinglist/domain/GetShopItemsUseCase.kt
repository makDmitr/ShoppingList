package com.example.shoppinglist.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class GetShopItemsUseCase(private val shopItemRepository: ShopItemRepository) {
    fun getShopItems(): LiveData<List<ShopItem>> {
        return shopItemRepository.getShopItems()
    }
}