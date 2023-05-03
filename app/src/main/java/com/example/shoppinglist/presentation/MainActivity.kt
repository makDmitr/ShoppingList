package com.example.shoppinglist.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), ShopItemFragment.OnFinishedEditingListener {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var viewModel: MainViewModel

    private lateinit var adapter: ShopItemsAdapter

    private var fcvShopItem: FragmentContainerView? = null

    private val isLandscape: Boolean
        get() {
            return fcvShopItem != null
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)

        fcvShopItem = findViewById(R.id.fcvShopItem)

        setupRecyclerView()

        if (isLandscape) {
            fabAdd.setOnClickListener {
                launchFragment(ShopItemFragment.newInstanceAddMode())
            }
        } else {
            fabAdd.setOnClickListener {
                val intent = ShopItemActivity.newIntentAddMode(this)
                startActivity(intent)
            }
        }

        viewModel.shopItemsList.observe(this) {
            adapter.submitList(it)
        }

    }

    private fun launchFragment(fragment: ShopItemFragment) {
        val fragmentManager = supportFragmentManager

        fragmentManager.popBackStack()
        fragmentManager.beginTransaction()
            .replace(R.id.fcvShopItem, fragment)
            .addToBackStack(null)
            .commit()
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
        setupRecyclerViewSwipes(rvShopItems)

        setupAdapterOnClickListener()
    }

    private fun setupAdapterOnClickListener() {
        if (isLandscape) {
            adapter.onClickListener = {
                launchFragment(ShopItemFragment.newInstanceEditMode(it.id))
            }
        } else {
            adapter.onClickListener = {
                val intent = ShopItemActivity.newIntentEditMode(
                    this,
                    it.id
                )
                startActivity(intent)
            }
        }
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

    private fun setupAdapterOnLongClickListener() {
        adapter.onLongClickListener = {
            viewModel.changeActiveState(it)
        }
    }

    override fun onFinishedEditing() {
        this.onBackPressed()
    }
}










































