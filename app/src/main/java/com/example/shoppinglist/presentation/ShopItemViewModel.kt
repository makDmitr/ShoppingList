package com.example.shoppinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglist.data.ShopItemRepositoryImpl
import com.example.shoppinglist.domain.*

class ShopItemViewModel : ViewModel() {
    private val repositoryImpl = ShopItemRepositoryImpl

    private val addShopItemUseCase = AddShopItemUseCase(repositoryImpl)
    private val editShopItemUseCase = EditShopItemUseCase(repositoryImpl)
    private val getShopItemByIdUseCase = GetShopItemByIdUseCase(repositoryImpl)

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() {
            return _shopItem
        }

    private val _inputErrorName = MutableLiveData<Boolean>()
    val inputErrorName: LiveData<Boolean>
        get() {
            return _inputErrorName
        }

    private val _inputErrorQuantity = MutableLiveData<Boolean>()
    val inputErrorQuantity: LiveData<Boolean>
        get() {
            return _inputErrorQuantity
        }

    private val _canCloseScreen = MutableLiveData<Unit>()
    val canCloseScreen: LiveData<Unit>
        get() {
            return _canCloseScreen
        }

    fun addShopItem(productName: String?, quantityStr: String?) {
        val name = parseName(productName)
        val quantity = parseQuantity(quantityStr)
        val isDataValid = validateInputData(name, quantity)
        if (isDataValid) {
            val shopItem = ShopItem(name, quantity, true)
            addShopItemUseCase.addShopItem(shopItem)
            _canCloseScreen.value = Unit
        }
    }

    fun editShopItem(productName: String?, quantityStr: String?) {
        val name = parseName(productName)
        val quantity = parseQuantity(quantityStr)
        val isDataValid = validateInputData(name, quantity)
        if (isDataValid) {
            val item = _shopItem.value
            item?.let {
                val shopItem = it.copy(name = name, quantity = quantity)
                editShopItemUseCase.editShopItem(shopItem)
                _canCloseScreen.value = Unit
            }
        }

    }

    fun getShopItem(id: Int) {
        val item = getShopItemByIdUseCase.getShopItemById(id)
        _shopItem.value = item
    }

    private fun validateInputData(name: String, quantity: Int): Boolean {
        return validateName(name) && validateQuantity(quantity)
    }

    private fun parseName(productName: String?): String {
        return productName?.trim() ?: ""
    }

    private fun parseQuantity(quantity: String?): Int {
        return try {
            quantity?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun validateName(name: String): Boolean {
        var validationResult = true
        if (name.isBlank()) {
            validationResult = false
            _inputErrorName.value = true
        }
        return validationResult
    }

    private fun validateQuantity(quantity: Int): Boolean {
        var validationResult = true
        if (quantity < 1) {
            validationResult = false
            _inputErrorQuantity.value = true
        }
        return validationResult
    }

    fun resetInputErrorName() {
        _inputErrorName.value = false
    }

    fun resetInputErrorQuantity() {
        _inputErrorQuantity.value = false
    }
}

























