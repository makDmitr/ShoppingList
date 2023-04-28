package com.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ShopItemActivity : AppCompatActivity() {

    private var screenMode = UNSPECIFIED_MODE
    private var shopItemId = ShopItem.UNSPECIFIED_ID
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)

        parseIntent()
        launchScreenInRightMode()
    }

    private fun launchScreenInRightMode() {
        val fragment = when (screenMode) {
            MODE_ADD -> {
                ShopItemFragment.newInstanceAddMode()
            }
            MODE_EDIT -> {
                ShopItemFragment.newInstanceEditMode(shopItemId)
            }
            else -> throw java.lang.RuntimeException(
                "ShopItemActivity can work only in mode=" +
                        "$MODE_ADD or mode=$MODE_EDIT. Current mode=$screenMode"
            )
        }
        supportFragmentManager.beginTransaction()
            .add(R.id.fcvShopItem, fragment)
            .commit()
    }

    private fun parseIntent() {
        if (!intent.hasExtra(KEY_MODE)) {
            throw java.lang.RuntimeException("There is no specified mode for ShopItemActivity")
        }

        val mode = intent.getStringExtra(KEY_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw java.lang.RuntimeException(
                "ShopItemActivity can work only in mode=" +
                        "$MODE_ADD or mode=$MODE_EDIT. Current mode=$mode"
            )
        }

        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(KEY_SHOP_ITEM_ID)) {
                throw java.lang.RuntimeException("You started ShopItemActivity in $MODE_EDIT, " +
                        "but didn't specify shopItemId")
            }
            shopItemId = intent.getIntExtra(KEY_SHOP_ITEM_ID, ShopItem.UNSPECIFIED_ID)
        }
    }

    companion object {
        const val MODE_ADD = "mode_add"
        const val MODE_EDIT = "mode_edit"

        private const val KEY_MODE = "key_mode"
        private const val KEY_SHOP_ITEM_ID = "shop_item_id"

        private const val UNSPECIFIED_MODE = ""

        fun newIntentAddMode(startFrom: Context): Intent {
            val intent = Intent(startFrom, ShopItemActivity::class.java)
            intent.putExtra(KEY_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditMode(startFrom: Context, shopItemId: Int): Intent {
            val intent = Intent(startFrom, ShopItemActivity::class.java)
            intent.putExtra(KEY_MODE, MODE_EDIT)
            intent.putExtra(KEY_SHOP_ITEM_ID, shopItemId)
            return intent
        }
    }
}