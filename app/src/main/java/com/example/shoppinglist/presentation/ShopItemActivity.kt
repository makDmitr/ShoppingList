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

    private lateinit var tilName: TextInputLayout
    private lateinit var tilQuantity: TextInputLayout
    //TextInput or EditText?
    private lateinit var etName: TextInputEditText
    private lateinit var etQuantity: TextInputEditText

    private lateinit var bSaveToList: Button

    private lateinit var viewModel: ShopItemViewModel

    private var screenMode = UNSPECIFIED_MODE
    private var shopItemId = ShopItem.UNSPECIFIED_ID
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()

        initViews()
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]

        addTextChangeListeners()
        launchScreenInRightMode()
        setViewModelObservers()
    }

    private fun launchScreenInRightMode() {
        when (screenMode) {
            MODE_ADD -> {
                launchScreenInAddMode()
            }
            MODE_EDIT -> {
                launchScreenInEditMode()
            }
        }
    }

    private fun launchScreenInAddMode() {
        bSaveToList.setOnClickListener {
            val inputName = etName.text.toString()
            val inputQuantity = etQuantity.text.toString()
            viewModel.addShopItem(inputName, inputQuantity)
        }
    }

    private fun launchScreenInEditMode() {
        viewModel.getShopItem(shopItemId)

        viewModel.shopItem.observe(this) {
            etName.setText(it.name)
            etQuantity.setText(it.quantity.toString())
        }

        bSaveToList.setOnClickListener {
            val inputName = etName.text.toString()
            val inputQuantity = etQuantity.text.toString()
            viewModel.editShopItem(inputName, inputQuantity)
        }
    }

    private fun setViewModelObservers() {
        viewModel.inputErrorName.observe(this) {
            if (it) {
                tilName.error = getString(R.string.error_name)
            } else {
                tilName.error = null
            }
        }

        viewModel.inputErrorQuantity.observe(this) {
            if (it) {
                tilQuantity.error = getString(R.string.error_quantity)
            } else {
                tilQuantity.error = null
            }
        }

        viewModel.canCloseScreen.observe(this) {
            finish()
        }
    }

    private fun addTextChangeListeners() {
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetInputErrorName()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        etQuantity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetInputErrorQuantity()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }


    private fun initViews() {
        tilName = findViewById(R.id.tilName)
        tilQuantity = findViewById(R.id.tilQuantity)
        etName = findViewById(R.id.etName)
        etQuantity = findViewById(R.id.etQuantity)
        bSaveToList = findViewById(R.id.bSaveToList)
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