package com.example.shoppinglist.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopItemRepository
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

object ShopItemRepositoryImpl: ShopItemRepository {

    //TODO 1) Данные у меня хранятся просто в виде TreeSet в оперативке, никаких БД и Серверов (всего 7 пунктов + вывод, открывай окно TODO)
    private val shopItems = sortedSetOf<ShopItem>({ o1, o2 ->
        o1.id.compareTo(o2.id)
    })
    private var newItemId = 0

    private val shopItemsLiveData = MutableLiveData<List<ShopItem>>()
    init {
        for (i in 0 until 5) {
            val currentShopItem = ShopItem("Name$i", i, Random.nextBoolean())
            addShopItem(currentShopItem)
        }
    }
    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNSPECIFIED_ID) {
            shopItem.id = newItemId++
        }
        shopItems.add(shopItem)

        //TODO: 2)Возвращаю я данные через обновление значения лайвдаты. Каждый раз при изменении данных я кладу в лайвдату _новый_ список, который является копией TreeSet
        shopItemsLiveData.value = shopItems.toList()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopItems.remove(shopItem)
        shopItemsLiveData.value = shopItems.toList()
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldShopItem = getShopItemById(shopItem.id)
        shopItems.remove(oldShopItem)
        addShopItem(shopItem)
    }

    override fun getShopItemById(id: Int): ShopItem {
        return shopItems.find {
            it.id == id
        } ?: throw java.lang.RuntimeException("There is no element with id=$id")
    }

    override fun getShopItems(): LiveData<List<ShopItem>> {
        return shopItemsLiveData
    }
}