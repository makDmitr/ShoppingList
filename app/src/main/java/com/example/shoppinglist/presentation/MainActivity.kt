package com.example.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        var tmp = false
    }

    private lateinit var viewModel: MainViewModel

    private lateinit var adapter: ShopItemsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setupRecyclerView()

        viewModel.shopItemsList.observe(this) {
            adapter.shopItems = it
        }

    }

    private fun setupRecyclerView() {
        val rvShopItems = findViewById<RecyclerView>(R.id.rvShopItems)
        adapter = ShopItemsAdapter()
        with(rvShopItems) {
            adapter = this@MainActivity.adapter
            layoutManager = LinearLayoutManager(this@MainActivity)

        }
    }


}