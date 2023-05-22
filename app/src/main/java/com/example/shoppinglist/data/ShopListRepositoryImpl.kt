package com.example.shoppinglist.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository

class ShopListRepositoryImpl(
    application: Application
) : ShopListRepository {

    private val shopListDAO = AppDatabase.geInstance(application).shopListDao()
    private val mapper = ShopListMapper()

    override suspend fun addShopItem(shopItem: ShopItem) {
        shopListDAO.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    override suspend fun deleteShopItem(shopItem: ShopItem) {
        shopListDAO.deleteShopItem(shopItem.id)
    }

    override suspend fun editShopItem(shopItem: ShopItem) {
        addShopItem(shopItem)
    }

    override suspend fun getShopItem(shopItemId: Int): ShopItem {
        val dbModel = shopListDAO.getShopItem(shopItemId)
        return mapper.mapDbModelToEntity(dbModel)
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
//        return MediatorLiveData<List<ShopItem>>().apply {
//            addSource(shopListDAO.getShopList()){
//                value = mapper.mapListDbModelToListEntity(it)
//            }
//        }
        return shopListDAO.getShopList().map { mapper.mapListDbModelToListEntity(it) }
    }
}