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

class ShopItemFragment(
    private val screenMode: String = UNSPECIFIED_MODE,
    private val shopItemId: Int = ShopItem.UNSPECIFIED_ID
) : Fragment() {

    private lateinit var tilName: TextInputLayout
    private lateinit var tilQuantity: TextInputLayout

    private lateinit var etName: TextInputEditText
    private lateinit var etQuantity: TextInputEditText

    private lateinit var bSaveToList: Button

    private lateinit var viewModel: ShopItemViewModel

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
        if (screenMode != MODE_ADD && screenMode != MODE_EDIT) {
            throw java.lang.RuntimeException(
                "ShopItemActivity can work only in mode=$MODE_ADD or mode=$MODE_EDIT." +
                        "Current mode is $screenMode"
            )
        }
        if (screenMode == MODE_EDIT && shopItemId == ShopItem.UNSPECIFIED_ID) {
            throw java.lang.RuntimeException(
                "You started ShopItemActivity in $MODE_EDIT, but didn't specify shopItemId."
            )
        }
    }

    companion object {
        const val MODE_ADD = "mode_add"
        const val MODE_EDIT = "mode_edit"

        fun newInstanceAddMode(): ShopItemFragment {
            return ShopItemFragment(MODE_ADD)
        }

        fun newInstanceEditMode(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment(MODE_EDIT, shopItemId)
        }

        private const val UNSPECIFIED_MODE = ""
    }
}