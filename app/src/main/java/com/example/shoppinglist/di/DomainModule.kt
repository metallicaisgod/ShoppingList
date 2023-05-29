package com.example.shoppinglist.di

import com.example.shoppinglist.data.ShopListRepositoryImpl
import com.example.shoppinglist.domain.ShopListRepository
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {

    @Binds
    fun bindShopListRepository(impl: ShopListRepositoryImpl): ShopListRepository
}