package com.example.shoppinglist.di

import android.app.Application
import com.example.shoppinglist.data.AppDatabase
import com.example.shoppinglist.data.ShopListDAO
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    fun provideShopListDao(application: Application): ShopListDAO {
        return AppDatabase.geInstance(application).shopListDao()
    }
}