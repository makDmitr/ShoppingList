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
    private val repositoryImpl = ShopItemRepositoryImpl

    private val getShopItemsUseCase = GetShopItemsUseCase(repositoryImpl)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repositoryImpl)
    private val editShopItemUseCase = EditShopItemUseCase(repositoryImpl)

    val shopItemsList = getShopItemsUseCase.getShopItems()

    fun deleteShopItem(shopItem: ShopItem) {
        deleteShopItemUseCase.deleteShopItem(shopItem)
    }

    fun changeActiveState(shopItem: ShopItem) {
        //TODO 5)!Вот сюда мне прилетает элемент из той же коллекции(="копии" TreeSet), которая прямо сейчас установлена в адаптере. Почему же не работает? См серый коммент ниже
        //Весь прикол в том, как именно копируются коллекции в Java/Kotlin в принципе. Какой бы метод копирования
        //ты не использовал: .toList(), Collections.copy() или копирование с помощью перегруженного конструктора, скажем ArrayList,
        //полученная коллекция-копия _не_ будет является deep копией оригинальной коллекции, т.е. когда у копии создается
        //свой, независимый набор объектов, которые просто имеют те же значения полей, что и объекты из оригинальной
        //коллекции [т.е. адреса в куче объектов старой и новой коллекции в этом случае отличались бы]
        //  Коллекция-копия _всегда_ будет являться shallow копией оригинальной коллекции, т.е. в новую
        //коллекцию просто скопируются ссылки на _те_же_ объекты, что уже хранит оригинальная коллекция.
        //[т.е. адреса в куче объектов старой и новой коллекции в этом случае совпадают].
        //TODO 6)Поэтому, когда в этой строчке ты меняешь, казалось бы, поле объекта-"копии" из List, который хранит адаптер, по факту ты сразу меняешь оригинал, который хранится в TreeSet
        // [Потому что List-копия и TreeSet-оригинал хранят ссылки на одни и те же объекты]
        shopItem.isActive = !shopItem.isActive
        //TODO Вывод: Скорее всего это было сделано для экономии рессурсов, чтобы при копировании коллекции каждый раз не создавать глубокую копию
        // _сразу_ для _всех_ объектов.
        // Видимо, подразумевается, что ты знаешь об этой особенности и когда тебе в коллекции-копии нужно сделать изменения с объектом
        // и при этом не затронуть оригинал, ты сам, вручную,
        // 1] создашь глубокуб копию (только этого, а не всех) объекта, 2] сделаешь изменения, 3] и будешь как-то его использовать, например, засетишь этот объект в коллекцию копию (на место предыдущего)
        //т.е. в данном случае нужно было бы сделать так: val newItem = shopItem.copyOf(isActive = !shopItem.isActive). Ну и дальше передать в метод, посмотри, если интересно, что там.
        //todo !Поэтому всегда, когда изменяешь объект в коллекции-копии - создавай копию этого объекта, чтобы не повлиять на оригинал.
        // Именно для этого, видимо, все поля в data-классах и рекомендуется делать val, так у тебя просто не останется выбора, ты всегда будешь вынужден
        // создавать копии объекта, чтобы его изменить [и никак не сможешь повлиять на оригинал]
        editShopItemUseCase.editShopItem(shopItem)
    }
}




































