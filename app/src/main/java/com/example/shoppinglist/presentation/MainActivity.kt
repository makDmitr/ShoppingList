package com.example.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    val listsInSubmitList = mutableListOf<List<ShopItem>>()

    private lateinit var viewModel: MainViewModel

    private lateinit var adapter: ShopItemsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setupRecyclerView()

        //TODO 3) В слушатель лайвдаты при изменении данных каждый раз прилетает новый immutable список-копия, вроде все и должно быть ок...
        viewModel.shopItemsList.observe(this) {
            listsInSubmitList.add(it)
            //TODO 7) И потом, когда ты сабмитишь в адаптер вроде бы новую коллекцию ,которая должна отличаться от предыдущей
            // по факту получается, что эти же изменения с объектами уже были сделаны и в коллекции, которая прямо сейчас лежит в адаптере.
            //[Ты предполагал, что у новой коллекции будут новые, независимые копии всех объектов из оригинальной коллекции
            //просто все эти объекты (кроме одного, у которого изменлось поле isActive) будут равны по equals() оригинальным, но
            //это не так из-за того, что копия является shallow-копией.]

            //(Ну и + косяк, даже если бы все работало, как ты думаешь, раз ты, где туду 6), меняешь поле объекта из листа, который в адаптере,
            //то, энивэй, измененнный объект в текущей коллекции адаптера был бы по equals() равен тому объекту, который прилетает в новой копии листа.
            //Но это не поинт всех этих to_do-комментов в данном случае.
            adapter.submitList(it)
        }

    }

    private fun setupRecyclerView() {
        val rvShopItems = findViewById<RecyclerView>(R.id.rvShopItems)
        adapter = ShopItemsAdapter()
        with(rvShopItems) {
            adapter = this@MainActivity.adapter
            layoutManager = LinearLayoutManager(this@MainActivity)

            recycledViewPool.setMaxRecycledViews(
                ShopItemsAdapter.ACTIVE_VIEW_TYPE,
                ShopItemsAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopItemsAdapter.NOT_ACTIVE_VIEW_TYPE,
                ShopItemsAdapter.MAX_POOL_SIZE
            )
        }

        setupAdapterOnLongClickListener()
        setupAdapterOnClickListener()
        setupRecyclerViewSwipes(rvShopItems)
    }

    private fun setupRecyclerViewSwipes(rvShopItems: RecyclerView) {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val shopItemToDelete = adapter.currentList[position]
                viewModel.deleteShopItem(shopItemToDelete)
            }
        })
        itemTouchHelper.attachToRecyclerView(rvShopItems)
    }

    private fun setupAdapterOnClickListener() {
        adapter.onClickListener = {
            Log.d("onClickListenerStub", "clicked: $it")
        }
    }

    private fun setupAdapterOnLongClickListener() {
        adapter.onLongClickListener = {
            //TODO: 4)...но неправильно отрабатывает этот слушатель. По долгому нажатию на элемент списка должен меняться флаг isActive у нажатого объекта ShopItem
            // и по итогу он должен перерисовываться в RecyclerView
            viewModel.changeActiveState(it)
        }
    }
}










































