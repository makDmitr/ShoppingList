package com.example.shoppinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglist.data.ShopItemRepositoryImpl
import com.example.shoppinglist.domain.DeleteShopItemUseCase
import com.example.shoppinglist.domain.EditShopItemUseCase
import com.example.shoppinglist.domain.GetShopItemsUseCase
import com.example.shoppinglist.domain.ShopItem

class MainViewModel: ViewModel() {

    //Так неправильно, но Dependency Injection пока не знаю.
    private val repositoryImpl = ShopItemRepositoryImpl()

    private val getShopItemsUseCase = GetShopItemsUseCase(repositoryImpl)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repositoryImpl)
    private val editShopItemUseCase = EditShopItemUseCase(repositoryImpl)

    private val shopItemsList = MutableLiveData<List<ShopItem>>()

    fun shopItemsList(): LiveData<List<ShopItem>> {
        return shopItemsList
    }

    fun getShopItems() {
        shopItemsList.value = getShopItemsUseCase.getShopItems()
    }
}