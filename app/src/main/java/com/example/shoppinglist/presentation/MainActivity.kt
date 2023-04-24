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

        viewModel.shopItemsList.observe(this) {
            listsInSubmitList.add(it)
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
            viewModel.changeActiveState(it)
        }
    }
}










































