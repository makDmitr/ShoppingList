package com.example.shoppinglist.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ShopItemFragment: Fragment() {

    private lateinit var tilName: TextInputLayout
    private lateinit var tilQuantity: TextInputLayout

    private lateinit var etName: TextInputEditText
    private lateinit var etQuantity: TextInputEditText

    private lateinit var bSaveToList: Button

    private lateinit var viewModel: ShopItemViewModel

    private var screenMode: String = UNSPECIFIED_MODE
    private var shopItemId: Int = ShopItem.UNSPECIFIED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_shop_item,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parseParams()

        initViews(view)
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

        viewModel.shopItem.observe(viewLifecycleOwner) {
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
        viewModel.inputErrorName.observe(viewLifecycleOwner) {
            if (it) {
                tilName.error = getString(R.string.error_name)
            } else {
                tilName.error = null
            }
        }

        viewModel.inputErrorQuantity.observe(viewLifecycleOwner) {
            if (it) {
                tilQuantity.error = getString(R.string.error_quantity)
            } else {
                tilQuantity.error = null
            }
        }

        viewModel.canCloseScreen.observe(viewLifecycleOwner) {
            activity?.onBackPressed()
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


    private fun initViews(view: View) {
        tilName = view.findViewById(R.id.tilName)
        tilQuantity = view.findViewById(R.id.tilQuantity)
        etName = view.findViewById(R.id.etName)
        etQuantity = view.findViewById(R.id.etQuantity)
        bSaveToList = view.findViewById(R.id.bSaveToList)
    }


    private fun parseParams() {
        val args = requireArguments()

        if (!args.containsKey(KEY_MODE)) {
            throw java.lang.RuntimeException("There is no specified mode for ShopItemFragment")
        }

        val mode = args.getString(KEY_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw java.lang.RuntimeException(
                "ShopItemFragment can work only in mode=" +
                        "$MODE_ADD or mode=$MODE_EDIT. Current mode=$mode"
            )
        }
        screenMode = mode

        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(KEY_SHOP_ITEM_ID)) {
                throw java.lang.RuntimeException("You started ShopItemFragment in $MODE_EDIT, " +
                        "but didn't specify shopItemId")
            }
            shopItemId = args.getInt(KEY_SHOP_ITEM_ID, ShopItem.UNSPECIFIED_ID)
        }
    }

    companion object {
        const val MODE_ADD = "mode_add"
        const val MODE_EDIT = "mode_edit"
        private const val UNSPECIFIED_MODE = "mode_unspecified"

        private const val KEY_MODE = "key_mode"
        private const val KEY_SHOP_ITEM_ID = "shop_item_id"

        fun newInstanceAddMode(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditMode(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_MODE, MODE_EDIT)
                    putInt(KEY_SHOP_ITEM_ID, shopItemId)
                }
            }
        }


    }
}