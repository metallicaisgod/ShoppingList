package com.example.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var llShopList: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        llShopList = findViewById(R.id.llShopList)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList.observe(this){
            showList(it)
        }
    }

    private fun showList(shopItems: List<ShopItem>) {
        llShopList.removeAllViews()
        for(item in shopItems){
            val layoutId = if(item.enabled){
                R.layout.item_shop_enabled
            } else {
                R.layout.tem_shop_disabled
            }

            val view = LayoutInflater.from(this)
                .inflate(layoutId, llShopList, false)
            view.findViewById<TextView>(R.id.tv_name).text = item.name
            view.findViewById<TextView>(R.id.tv_count).text = item.count.toString()
            view.setOnLongClickListener {
                viewModel.changeEnableState(item)
                true
            }
            llShopList.addView(view)
        }
    }
}