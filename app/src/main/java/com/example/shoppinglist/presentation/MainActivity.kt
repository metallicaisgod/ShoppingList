package com.example.shoppinglist.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.ActivityMainBinding
import javax.inject.Inject

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    private val component by lazy {
        (application as ShoppingListApp).component
    }

    private lateinit var shopListAdapter: ShopListAdapter
    private var shopItemContainer: FragmentContainerView? = null

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (binding.shopItemFragmentContainer == null)
            supportFragmentManager.popBackStack()
        setupRecyclerView()
        viewModel.shopList.observe(this) {
            shopListAdapter.submitList(it)
        }
        binding.buttonAddShopItem.setOnClickListener {
            if (isOnePanelMode()) {
                val intent = ShopItemActivity.newIntentAddItem(this)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.addNewFragmentAddItem())
            }
        }
//        val cursor = contentResolver.query(
//            Uri.parse("content://com.example.shoppinglist/shop_items"),
//            null,
//            null,
//            null,
//            null,
//            null
//        )
//
//        while(cursor?.moveToNext() == true){
//            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
//            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
//            val count = cursor.getInt(cursor.getColumnIndexOrThrow("count"))
//            val enabled = cursor.getInt(cursor.getColumnIndexOrThrow("enabled")) > 0
//
//            val shopItem = ShopItem(name, count, enabled, id)
//            Log.d("contentResolver", shopItem.toString())
//        }
//        cursor?.close()
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun isOnePanelMode(): Boolean {
        return shopItemContainer == null
    }

    private fun setupRecyclerView() {
        shopListAdapter = ShopListAdapter()
        with(binding.rvShopList) {
            adapter = shopListAdapter
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.ENABLED_ITEM,
                ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.DISABLED_ITEM,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }
        setupLongClickListener()
        setupClickListener()
        setupSwipeListener(binding.rvShopList)
    }

    private fun setupSwipeListener(rvShopList: RecyclerView) {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
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
                val item = shopListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
//                thread {
//                    contentResolver.delete(
//                        Uri.parse("content://com.example.shoppinglist/shop_items"),
//                        null,
//                        arrayOf(item.id.toString())
//                    )
//                }

                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.note_deleted, item.name),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        ).attachToRecyclerView(rvShopList)
    }

    private fun setupClickListener() {
        shopListAdapter.onShopItemClickListener = {
            if (isOnePanelMode()) {
                val intent = ShopItemActivity.newIntentEditItem(this, it.id)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.addNewFragmentEditItem(it.id))
            }
        }
    }

    private fun setupLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }

    override fun onEditingFinished() {
        Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
    }
}